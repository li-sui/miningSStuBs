# A Study of Single Statement Bugs (SStuBs) Involving Dynamic Language Features

* `src/main/resources/allJavaProjects.csv` - contains list of url of Java repositories on Github.
* to clone all repositories in the above list, run `nz.ac.massey.se.dynLangInSStuBs.tasks.CloneRepositories `
*  run SStuBs miner on cloned repositories: `nz.ac.massey.se.dynLangInSStuBs.tasks.SStuBsMiner`
<!--* githubCommunityProjects.csv - list of projects from the extended dataset
* result.csv -raw result
* data.zip - additional dataset
* ManySStuBs4J dataset: https://zenodo.org/record/3653444#.YNVN4ZozZhE-->

* list of reflective API are selected from the paper `Java reflection API. ref: Challenges for Static Analysis of Java Reflection`. 
Note 3 API has been removed because they are not callsite: .class,  = and != 
