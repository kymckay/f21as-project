## Setup ##

The project is set up using Maven for ease of managing unit tests in an IDE agnostic way (including their continuous integration execution). As the intention isn't to build and distribute this project the rest of Maven's tooling isn't all that relevant to us.

### Eclipse ###

1. In Eclipse from the "File" menu select the "Import" option.
2. Under the "Maven" folder select the "Existing Maven Projects" option.
3. Set the "Root Directory" to your local copy of this repository and click "Finish".

### Visual Studio Code ###

1. Ensure you have support for Java installed (this handles installation of all needed extensions).
2. Open your local copy of this repository from "File" > "Open Folder...".


## Testing ##

Our unit tests are written using JUnit and can be found under the `src\test\java` directory. These are configured to run with GitHub Actions on each push to the repository and will block PRs from being merged if any are failing.

GitHub Actions configuration is found under the `.github\workflows` directory.