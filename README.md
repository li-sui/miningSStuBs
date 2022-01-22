# A Study of Single Statement Bugs (SStuBs) Involving Dynamic Language Features

* `src/main/resources/allJavaProjects.csv` - contains list of url of Java repositories on Github.
* to clone all repositories in the above list, run `nz.ac.massey.se.dynLangInSStuBs.tasks.CloneRepositories `
* run SStuBs miner on cloned repositories: `nz.ac.massey.se.dynLangInSStuBs.tasks.SStuBsMiner`
* analysis scripts
    1. unzip `src/main/resources/sstubs.zip`
    2. checkout all source code `nz.ac.massey.se.dynLangInSStuBs.sourceCodeAnalysis.SourceCodeRetriever`
    3. check for FPs `nz.ac.massey.se.dynLangInSStuBs.sourceCodeAnalysis.checkFP`



<!--* githubCommunityProjects.csv - list of projects from the extended dataset
* result.csv -raw result
* data.zip - additional dataset
* ManySStuBs4J dataset: https://zenodo.org/record/3653444#.YNVN4ZozZhE-->

* `src/main/resources/reflectiveAPIs.json` - list of reflective API are selected from the paper 
`Sui, L., Dietrich, J., Emery, M., Rasheed, S., & Tahir, A. (2018, December). On the soundness of call graph construction in the presence of dynamic language features-a benchmark and tool evaluation. In Asian Symposium on Programming Languages and Systems (pp. 69-88). Springer, Cham.`
and `Landman, D., Serebrenik, A., & Vinju, J. J. (2017, May). Challenges for static analysis of java reflection-literature review and empirical study. In 2017 IEEE/ACM 39th International Conference on Software Engineering (ICSE) (pp. 507-518). IEEE.`.
