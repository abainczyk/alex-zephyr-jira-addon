# ALEX for Jira (Add-on)

*ALEX for Jira* is an add-on for Jira Server, written in Java, that allows the execution of *Zephyr* tests in the test automation tool [ALEX][alex].
For this to work, it requires a running installation of the [ALEX for Jira adapter][alex-adapter] and ALEX.
This add-on serves two purposes:

* It embeds a button to the operations bar of a Zephyr test.
  The button enables users to execute the test in ALEX, under the assumption that a mapping between the test and ALEX has been defined in the adapter.
* It listens on issue and project related events in Jira and sends them to the adapter in order for the tests to stay in sync.

## Requirements

### Development

* Java JDK 8
* Git
* [Atlassian SDK][atlassian-sdk]

For the installation of the Atlassian SDK, follow the instructions provided on the website.
After the installation the command `atlas-mvn` should be available in the terminal.

### Dependencies

The add-on has been developed with, but is not necessarily limited to the following software versions:

* Windows 10
* Java JRE 8
* [Jira Server][jira] v7.9.2 with:
    * [Zephyr for Jira][zephyr] v3.6.4 add-on
    * [ZAPI][zapi] v2.6.0 add-on
* [ALEX][alex] v1.6.0
* [ALEX Adapter][alex-adapter] v1.0.0

Note that if ALEX and Jira are installed on different machines in the same network, make sure they can reach each other.

## Build instructions

```bash
# 1. Clone the repository
git clone ...

# 2. Navigate to the project directory
cd alex-zephyr-jira-plugin

# 3. Build the plugin
atlas-mvn clean package
```

The generated add-on is then be located in the `target` directory.
The file is called `alex-for-jira-1.0.0.jar`.


## Installation

1. Build the add-on according to the description above.
2. Open Jira and login with an admin account and navigate to the add-ons page.
3. In the left sidebar, click on *"Manage add-ons"*.
4. Click on the button *"Upload add-on"* and upload the jar file generated from the build step.
5. After the successful installation, refresh the page.
   Now, a section *ALEX FOR JIRA* with one item should appear in the left sidebar.


## Setup

Before setting up the add-on, ensure that there is a running installation of ALEX and the adapter and that all servers can reach each other.
In the following, we assume that the adapter is installed locally at *http://localhost:9000*.

1. Install the add-on.
2. In the left sidebar on the add-ons page, click on the item *"General Configuration"* under the *"ALEX FOR JIRA"* section.
3. In the displayed form
    1. Enter the base URL of the ALEX adapter, without trailing *"/"*.
       E.g. *http://localhost:9000* and not *http://localhost:9000/*.
    2. Enter the email and password of an admin account in the ALEX adapter.
    3. Select the correct issue type for tests that is registered by the Zephyr add-on from the dropdown menu.
       Normally, this is the type *"Test"*.
    4. Enter the **key** of the project that the add-on should be activated for.
4. Click on save.

Now, on every issue that is a Zephyr test, a dropdown menu with the label *"ALEX"* should be visible in the operations bar.


[alex]: https://github.com/learnlib/alex
[alex-adapter]: https://bitbucket.org/abainczyk/alex-zephyr-jira-adapter
[jira]: https://de.atlassian.com/software/jira/download
[zephyr]: https://marketplace.atlassian.com/apps/1014681/zephyr-for-jira-test-management?hosting=server
[zapi]: https://marketplace.atlassian.com/apps/1211674/zapi?hosting=server&tab=overview
[atlassian-sdk]: https://developer.atlassian.com/server/framework/atlassian-sdk/
