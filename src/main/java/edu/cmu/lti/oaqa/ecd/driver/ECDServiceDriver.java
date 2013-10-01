package edu.cmu.lti.oaqa.ecd.driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mx.bigdata.anyobject.AnyObject;
import mx.bigdata.anyobject.AnyTuple;
import net.sf.saxon.Transform;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.aae.controller.AnalysisEngineController;
import org.apache.uima.aae.jmx.monitor.JmxMonitor;
import org.apache.uima.adapter.jms.JmsConstants;
import org.apache.uima.adapter.jms.activemq.SpringContainerDeployer;
import org.apache.uima.adapter.jms.service.UIMA_Service;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.resourceSpecifier.factory.DeploymentDescriptorFactory;
import org.apache.uima.resourceSpecifier.factory.ServiceContext;
import org.apache.uima.resourceSpecifier.factory.UimaASPrimitiveDeploymentDescriptor;
import org.apache.uima.resourceSpecifier.factory.impl.ServiceContextImpl;
import org.apache.uima.util.Level;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.google.common.collect.Maps;

import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.config.ConfigurationLoader;
import edu.cmu.lti.oaqa.ecd.flow.FunneledFlow;
import edu.cmu.lti.oaqa.ecd.flow.strategy.FunnelingStrategy;

public class ECDServiceDriver extends UIMA_Service {

//  private static final String DEPLOYMENT_DESCRIPTOR_TEMPLATE = "service/DeploymentDescriptorTemplate";

  private static final String DD2SPRING_XSL = "/service/dd2spring.xsl";

  private static final Class CLASS_NAME = ECDServiceDriver.class;

  private static boolean DEBUG_SERVICE_CONFIG = false;

  @Override
  public String[] initialize(String[] args) throws Exception {

    DEBUG_SERVICE_CONFIG = Boolean.parseBoolean(System.getProperty(
            "ECDServiceDriver.DEBUG_SERVICE_CONFIG", "false"));

    // TODO needs major clean up of tmp files
    URL xsltResource = getClass().getResource(DD2SPRING_XSL);
    File xslTransform = File.createTempFile(FilenameUtils.getBaseName(xsltResource.getFile()),
            FilenameUtils.getExtension(xsltResource.getFile()));
    if (!DEBUG_SERVICE_CONFIG) {
      xslTransform.deleteOnExit();
    }
    IOUtils.copy(xsltResource.openStream(), FileUtils.openOutputStream(xslTransform));

    String[] springConfigFileArray = new String[args.length];
    for (int i = 0; i < args.length; i++) {
      String resource = args[i];
      AnyObject config = ConfigurationLoader.load(resource);

      AnalysisEngineDescription description = createServiceAEDescription(resource, config);
      File aeDescFile = File.createTempFile(resource, ".xml");
      if (!DEBUG_SERVICE_CONFIG) {
        aeDescFile.deleteOnExit();
      }
      description.toXML(new FileOutputStream(aeDescFile));
      String deployDescFile = createServiceDeploymentDescriptor(resource, config,
              aeDescFile.getAbsolutePath());
      File springConfigFile = convertDd2SpringDesc(deployDescFile, xslTransform.getAbsolutePath());

      if (null == springConfigFile) {
        return null;
      }
      springConfigFileArray[i] = springConfigFile.getAbsolutePath();

      if (System.getProperty(JmsConstants.SessionTimeoutOverride) != null) {
        System.out.println(">>> Setting Inactivity Timeout To: "
                + System.getProperty(JmsConstants.SessionTimeoutOverride));
      }
    }

    return springConfigFileArray;
  }

  static AnalysisEngineDescription createServiceAEDescription(String resource, AnyObject config)
          throws Exception {
    if (config.getIterable("pipeline") == null) {
      return createComponentDescription(config);
    } else {
      return createServicePipelineDescription(resource, config);
    }
  }

  // TODO refactor
  static AnalysisEngineDescription createServicePipelineDescription(String resource,
          AnyObject config) throws Exception {
    TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
    String uuid = UUID.randomUUID().toString();
    BaseExperimentBuilder builder = new BaseExperimentBuilder(uuid, resource, typeSystem);
    FunnelingStrategy ps = ECDDriver.getProcessingStrategy(config);
    FunneledFlow funnel = ps.newFunnelStrategy(builder.getExperimentUuid());
    AnalysisEngineDescription pipeline = builder.buildPipelineDescription(config, "pipeline", 1,
            funnel, false, BaseExperimentBuilder.DEFAULT_CLASS_TAG);
    return pipeline;
  }

  static AnalysisEngineDescription createComponentDescription(AnyObject config) throws Exception {
    TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
    Map<String, Object> tuples = Maps.newLinkedHashMap();
    Class<? extends AnalysisComponent> ac = BaseExperimentBuilder.getFromClassOrInherit(config,
            AnalysisComponent.class, tuples);
    Object[] params = BaseExperimentBuilder.getParamList(tuples);
    TypePriorities typePriorities = BaseExperimentBuilder.loadTypePriorities(config);
    AnalysisEngineDescription description = AnalysisEngineFactory.createPrimitiveDescription(ac,
            typeSystem, typePriorities, params);
    return description;
  }

  static String createServiceDeploymentDescriptor(String resource, AnyObject config,
          String aeDescPath) throws IOException, ResourceInitializationException {
    
    String deployConfig = config.getString("deploy");
    Yaml yaml = new Yaml(new Constructor(ServiceContextBeanImpl.class));
    ServiceContext context = (ServiceContext) yaml.load(deployConfig.toString());
    context.setDescriptor(aeDescPath);
    if(context.getEndpoint() == null || context.getEndpoint().isEmpty()){
      context.setEndpoint(resource);
    }
    
    // create DD with default settings
    UimaASPrimitiveDeploymentDescriptor dd = DeploymentDescriptorFactory
            .createPrimitiveDeploymentDescriptor(context);

    // Get default Error Handler for Process
    dd.getProcessErrorHandlingSettings().setThresholdCount(4);

    // Two instances of AE in a jvm
    //dd.setScaleup(2);

    // Generate deployment descriptor in xml format
    String ddXML = dd.toXML();
    // Write the DD to a temp file
    File ddFile = File.createTempFile("Deploy_"+resource, ".xml");
    if (!DEBUG_SERVICE_CONFIG) {
      ddFile.deleteOnExit();
    }
    BufferedWriter out = new BufferedWriter(new FileWriter(ddFile));
    out.write(ddXML);
    out.close();

    return ddFile.getAbsolutePath();
  }

  public static File convertDd2SpringDesc(String ddFilePath, String dd2SpringXsltFilePath)
          throws Exception {
    File tempFile = File.createTempFile("convertedSpringDesc", ".xml");
    if (!DEBUG_SERVICE_CONFIG) {
      tempFile.deleteOnExit();
    }
    List<String> argsForSaxon = new ArrayList<String>();
    argsForSaxon.add("-l"); // turn on line numbers
    argsForSaxon.add("-s"); // source file
    argsForSaxon.add(ddFilePath); // source file
    argsForSaxon.add("-o"); // output file
    argsForSaxon.add(tempFile.getAbsolutePath()); // output file
    argsForSaxon.add(dd2SpringXsltFilePath); // xslt transform to apply
    Transform.main(argsForSaxon.toArray(new String[argsForSaxon.size()]));
    return tempFile;
  }

  public static void main(String[] args) {

    try {
      ECDServiceDriver service = new ECDServiceDriver();

      // parse command args and run dd2spring to generate spring context
      // files from deployment descriptors
      String contextFiles[] = service.initialize(args);

      // If no context files generated there is nothing to do
      if (contextFiles == null) {
        return;
      }
      // Deploy components defined in Spring context files. This method blocks
      // until
      // the container is fully initialized and all UIMA-AS components are
      // succefully
      // deployed.
      SpringContainerDeployer serviceDeployer = service.deploy(contextFiles);

      if (serviceDeployer == null) {
        System.out.println(">>> Failed to Deploy UIMA Service. Check Logs for Details");
        System.exit(1);
      }
      // Add a shutdown hook to catch kill signal and to force quiesce and stop

      ServiceShutdownHook shutdownHook = new ServiceShutdownHook(serviceDeployer);
      Runtime.getRuntime().addShutdownHook(shutdownHook);

      // Check if we should start an optional JMX-based monitor that will
      // provide service metrics
      // The monitor is enabled by existence of
      // -Duima.jmx.monitor.interval=<number> parameter. By
      // default
      // the monitor is not enabled.
      String monitorCheckpointFrequency;
      if ((monitorCheckpointFrequency = System.getProperty(JmxMonitor.SamplingInterval)) != null) {
        // Found monitor checkpoint frequency parameter, configure and start the
        // monitor.
        // If the monitor fails to initialize the service is not effected.
        service.startMonitor(Long.parseLong(monitorCheckpointFrequency));
      }
      AnalysisEngineController topLevelControllor = serviceDeployer.getTopLevelController();
      String prompt = "Press 'q'+'Enter' to quiesce and stop the service or 's'+'Enter' to stop it now.\nNote: selected option is not echoed on the console.";
      if (topLevelControllor != null) {
        System.out.println(prompt);
        // Loop forever or until the service is stopped
        while (!topLevelControllor.isStopped()) {
          if (System.in.available() > 0) {
            int c = System.in.read();
            if (c == 's') {
              service.stopMonitor();
              serviceDeployer.undeploy(SpringContainerDeployer.STOP_NOW);
            } else if (c == 'q') {
              service.stopMonitor();
              serviceDeployer.undeploy(SpringContainerDeployer.QUIESCE_AND_STOP);
            } else if (Character.isLetter(c) || Character.isDigit(c)) {
              System.out.println(prompt);
            }
          }
          // This is a polling loop. Sleep for 1 sec
          try {
            if (!topLevelControllor.isStopped())
              Thread.sleep(1000);
          } catch (InterruptedException ex) {
          }
        } // while
      }
    } catch (Exception e) {
      if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
        UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "main",
                JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", e);
      }
    }

  }

  static class ServiceShutdownHook extends Thread {

    public SpringContainerDeployer serviceDeployer;

    public ServiceShutdownHook(SpringContainerDeployer serviceDeployer) {
      this.serviceDeployer = serviceDeployer;
    }

    public void run() {
      try {
        AnalysisEngineController topLevelController = serviceDeployer.getTopLevelController();
        if (topLevelController != null && !topLevelController.isStopped()) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "run",
                  JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_caught_signal__INFO",
                  new Object[] { topLevelController.getComponentName() });
          serviceDeployer.undeploy(SpringContainerDeployer.QUIESCE_AND_STOP);
        }
      } catch (Exception e) {
        if (UIMAFramework.getLogger(CLASS_NAME).isLoggable(Level.WARNING)) {
          UIMAFramework.getLogger(CLASS_NAME).logrb(Level.WARNING, CLASS_NAME.getName(), "run",
                  JmsConstants.JMS_LOG_RESOURCE_BUNDLE, "UIMAJMS_exception__WARNING", e);
        }
      }
    }
  }

}
