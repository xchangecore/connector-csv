Initial check-in for csv adapter.

2018-02-24:
    - Correct the proper behavior when the auto.close set to false

2018-02-22:
    - Correct the error message display.

2018-02-21: commit: d37d1f3b3e0dce8d10ce081cab1260c9313f281e
    - To test all the configuration files using the test data.
    - To avoid the incident creationg while there is duplicate index existed.

2018-01-18:	commit: bd1bdfcea7ca7397cedce2c8cb26e822225779ef
	- Make sure the description field not needed as long as the full.description is set to true.
	- Throw exception when there are more than one field has same column defined.

2018-01-01:
	- Added 'full.description' field to support the description including every columns in the csv file.
	- Example entry: 'full.description, true' in config file.

2017-04-24:
- Modified the Configuration's toMap to report the Index column properly.
- Added boyd and walmart config/data.

2017-04-20:
- Change the Configuration file autoClose default value to true for backward-compatible.

2017-04-18:
- Check-in the xcadapter.war

2016-12-08:
- to add the multiple configuration sections within a configuration file.
- to add 'auto.close' support for deletion.
- to add sample_config as the example for the config.
