---
layout: post
title: "Using Big Data for Good"
subtitle: "Big Data, it's not just for selling ads anymore"
header-img: "img/mon-ankeny.jpg"
author: "David Dooling"
githubProfile : "ddgenome"
twitterHandle : "ddgenome"
avatarUrl : "https://avatars1.githubusercontent.com/u/57881?v=3"
tags: [big data, genomics, IoT, precision farming]
---

Reading through blog posts, press releases, and source code you can easily get
the impression that everyone working in the Big Data space is focused on
determining what ads you are most likely to click.  From Facebook to Google to
Twitter, the modern players in IT collect massive amounts of information on
their customers and then analyze that data to provide ever more relevant ads.
As Jeff Hammeracher, formerly of Facebook and now with Cloudera, famously said,
&ldquo;The best minds of my generation are thinking about how to make people click
ads.&rdquo;  The result of this nearly singular focus is that many of the tools
developed to operate in the Big Data space aren't easily applied to systems
that are much more complicated than counting words and providing links to ads.
As anyone who has tried to use NoSQL databases for complicated work flows on
distributed systems can tell you, pushing the limits of these systems that
eschew features for speed can be fraught with peril, forcing you to do a lot of
engineering on your own.

But engineering is not the only problem. Sometimes, the data just aren't
enough.  For example, when I was working at [The Genome
Institute](http://genome.wustl.edu/), now the McDonnell Genome Institute, we
collected petabytes and petabytes of genomic data, comprising the sum of data
and analysis from [The Human Genome Project](http://www.genome.gov/10001772),
subsequent model genome references like mouse and chimpanzee, the [first whole
cancer genomes](http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2603574/), the
[1000 Genomes Project](http://www.1000genomes.org/), [The Cancer Genome
Atlas](http://cancergenome.nih.gov/), the [Pediatric Cancer Genome
Project](http://www.pediatriccancergenomeproject.org/site/), the [Human
Microbiome Project](http://hmpdacc.org/), and many others.  While all of these
efforts generated a lot of data and insights, they didn't cure cancer.
Sometimes the systems we are dealing with, whether they be cancerous cells
inside the complex environment that is the human body or producing fruit in the
complex environment of soil, pests, and weather, provide such challenges to our
understanding that simply collecting more and more data does not bring us
closer to answering our questions or solving our problems.  In these complex
systems, we are much more limited by our understanding of the many variables
involved and how they interact than we are by our ability to collect data.  In
these cases, we need to marry Big Data techniques with models of these systems.

Unfortunately, even our models of systems as complex as the human body and the
environment are often too simple to be predictive.  This provides a real
opportunity for Big Data in the land of complex science.  To date, Big Data has
focused primarily on collecting as much data as you can, worrying about how you
will use it later.  For these complex systems, this approach often just leads
to more noise.  By combining the scientific method with the machinery of Big
Data, we can design large-scale experiments capable of collecting massive
amounts of data that provide answers to specific questions, chosen to create
increased understanding of these complex systems.  In other words, these
experiments can be designed to create better models.  These models can then be
further refined with the next round of experiments, and so on.  This is exactly
the approach being used in genomics by the McDonnell Genome Institute and
[DOE's Joint Genome Institute](http://jgi.doe.gov/) as they try to map features
of human, plant, and microbial genomes to biological function.

Executing these directed Big Data experiments has only recently become possible
in genomics because of the ever decreasing cost of computing and storage and
the [precipitous drop in the cost of DNA
sequencing](https://www.genome.gov/sequencingcosts/) over the last decade (see
graph below).  For those outside the genomics world, similar changes are
occurring now with the [Internet of Things
(IoT)](http://whatis.techtarget.com/definition/Internet-of-Things).  As
everyday objects like refrigerators and thermostats are becoming
Internet-connected smart devices, the cost of incorporating this technology in
farming equipment large, e.g., combines, and small, e.g., water sensors, is
dropping rapidly.  The resulting ability to collect a wide variety of data
types rapidly, cheaply, and at scale completely changes our ability to measure
the environment in which a plant grows.

![Cost of DNA Sequencing over time](https://www.genome.gov/images/content/cost_megabase_.jpg)

At Monsanto, we are in a unique position to use directed Big Data
experiments on a large scale.  We can combine plant and microbial
genomics, soil science, and weather models informed by data collected
from Internet-connected planters, sensors, and combines into our IoT
platform to make significant improvements in our understanding of how
to create healthier plants and more optimal yields.  We call this
effort &ldquo;unlocking digital yield&rdquo; and it provides the basis
of how we intend to meet the needs of an ever hungrier planet.
Executing these experiments requires expertise across science,
statistics, and IT, both software and hardware.  We'll be writing more
detailed posts about our approach in these areas soon, but until then
here are some slides from a talk Rob Long, one of our Data Architects,
gave at [StampedeCon](http://stampedecon.com/) 2014 about how we use
[HBase](http://hbase.apache.org/) and
[Solr](http://lucene.apache.org/solr/) to analyze our large genomics
data sets.

<iframe src="//www.slideshare.net/slideshow/embed_code/key/15WPnDtRZ1wquL" width="595" height="485" frameborder="0" marginwidth="0" marginheight="0" scrolling="no" style="border:1px solid #CCC; border-width:1px; margin-bottom:5px; max-width: 100%;" allowfullscreen> </iframe> <div style="margin-bottom:5px"> <strong> <a href="//www.slideshare.net/StampedeCon/managing-genomes-at-scale-what-we-learned-stampedecon-2014" title="Managing Genomes At Scale: What We Learned - StampedeCon 2014" target="_blank">Managing Genomes At Scale: What We Learned - StampedeCon 2014</a> </strong> from <strong><a href="//www.slideshare.net/StampedeCon" target="_blank">StampedeCon</a></strong> </div>

Despite the prevailing notions in the popular press, there are a lot
of great opportunities in Big Data beyond just selling ads.  There are
complicated problems that not only do we not have the answers for, we
are still trying to figure out what the solutions will look like.
Solving these problems requires a diverse set of skills, and working
in close collaboration with a variety of experts is one of the great
things about working in IT at Monsanto.  When you couple that with
solving real-world problems that affect tens of millions of people
like cancer, or hundreds of millions of people like producing safe,
abundant food, working on Big Data can mean a lot more than just
getting a big paycheck.
