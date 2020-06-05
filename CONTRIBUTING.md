# Git Versioner Project and Developer Guidelines
Follow these easy steps and guidelines to ensure swift acceptance of pull requests into Git Versioner and the improved likelihood of seeing your changes reflected in a release version sooner.

## Contribution Workflow
Following the contribution workflow will ensure that your development won't go unnoticed and will be merged into the main repository in a quicker timeframe than if the workflow isn't followed.

1. Open an issue in GitHub with the bug, improvement, or feature request with information about your proposed changes. This will allow discussion over the changes to occur.

2. Fork the repository.

3. Make your changes and submit a pull request from your fork into master branch.

4. The pull request will run through GitHub Actions to ensure it compiles and tests pass. 

5. The pull request will be reviewed and comments made and changes requests where required.

5. When ready, the pull request will be merged into master branch and automatically included in the next release.

## Contribution Guidelines

Contributions will be merged in a lot quicker if you follow these guidelines:

1. Include unit tests for all new logic introduced.

2. Integration tests should be included where sensible.

3. Include documentation in the README for new features. 

## Commit Message Guidelines

We use commit messages as a form of documentation, so it's important that they contain enough information to be useful.

1. Use a concise title for the commit summary line.

2. Include extra information in the main body of the commit message. This can be in bullet points or sentences, and is supposed to give context around why the change was made and reasons for the design decisions made, and any shortcomings of the solution that may need to be looked at again.

3. Keep commits discrete and self-contained. Change one thing per commit, and ensure each commit makes sense in isolation.
