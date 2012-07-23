uima-ecd
========

Experiment Configuration Description for UIMA pipelines. 

The Experiment Configuration Description (ECD) is a set of capabilities built on top of UIMA+uimaFit, that allows the creation of UIMA pipelines form a configuration file.


An ECD can have the following sections:
 - experiment
 - collection-reader
 - pipeline
 - post-process

Resources within a pipeline are specified using either an inherit or a class mapping:

 - inherit: will look for a file WITHIN the classpath on the path specified by the doted syntax a.b.c => a/b/c 
            inherited properties can be overridden directly on the body of the declaration

 - class: will look for a class on the classpath, and is intended as a shortcut for classes that don't have 
          configurable parameters

The resources are configured by specifiying primitive parameters (Integer, Float, Long, Double, Boolean, String). Compound parameters are typically passed as Strings and parsed within the resource, this is usually the case of nested Resources.  

A pipeline is typically a sequence of phases that inherit from ecd.phase (edu.cmu.lti.oaqa.ecd.phase.BasePhase), or some of its sub-classes. A phase provides all the combinatorial functionality required for experimentation, but any arbitrary AnalysisEngine or CasConsumer can be inserted at any point in the pipleline.

Parameters within a phase can have additional combinatorial options.

Components that need to collect information form the different all the experimental options are specified at the end of the pipeline on the post-process section.

# Non-staged example

experiment:
  name: test-experiment
  author: junit
  
pipeline:
  - inherit: ecd.phase
    name: first-phase
    options: |
      - inherit: test.first-phase-annotator-a
      - class: edu.cmu.lti.oaqa.ecd.example.FirstPhaseAnnotatorB1 
      - pipeline: 
        - class: edu.cmu.lti.oaqa.ecd.example.FirstPhaseAnnotatorB1
        - inherit: test.first-phase-annotator-copts
        - pipeline: [inherit: test.first-phase-annotator-a, inherit: test.first-phase-annotator-copts2]  
      
  - inherit: ecd.phase
    name: second-phase  
    options: |
      - class: edu.cmu.lti.oaqa.ecd.example.SecondPhaseAnnotatorA1
      - class: edu.cmu.lti.oaqa.ecd.example.SecondPhaseAnnotatorB1
      - class: edu.cmu.lti.oaqa.ecd.example.SecondPhaseAnnotatorC1

  - inherit: ecd.phase
    name: third-phase  
    options: |
      - inherit: test.third-phase-annotator

post-process: 
  - inherit: eval.retrieval-measures-evaluator
  - inherit: report.csv-report-generator
    builders: |
      - inherit: report.f-measure-report-component 

A final note on YAML syntax, indentation is relevant to determine nesting of elements, and some characters  are reserved (-,:) so use quotes "" to use them on strings