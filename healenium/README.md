# Python with Healenium

Python 3.9.5 + Pytest project with healenium usage example 

[1. Start Healenium components](#1-start-healenium-components)
* [Healenium with Selenium-Grid](#run-healenium-with-selenium-grid)


[2. Configuration RemoteWebDriver for Healenium](#2-configuration-remotewebdriver-for-healenium)

[3. Run test](#3-run-test)

[4. Test Report](#4-test-report)

## How to start

### 1. Start Healenium components

Go into healenium folder

```sh
cd healenium
```

#### Run Healenium with Selenium-Grid:
```sh
docker-compose up -d
```


<b>ATTENTION</b>

Verify the next images are <b>Up</b> and <b>Running</b>
- `postgres-db` (PostgreSQL database to store etalon selector / healing / report)
- `hlm-proxy` (Proxy client request to Selenium server)
- `hlm-backend` (CRUD service)
- `selector imitator` (Convert healed locator to convenient format)
- `selenoid`/`selenium-grid` (Selenium server)

### 2. Configuration RemoteWebDriver for Healenium

To run using Healenium create RemoteWebDriver with URL ```http://<remote webdriver host>:8085```:

```py
        options = webdriver.ChromeOptions()
        self.driver = webdriver.Remote('http://localhost:8085', options=options)
```

To temporarily disable the healing mechanism for certain sections of your code, use the following syntax:

```py
        self.driver.execute_script("disable_healing_true")
        ... // Your code that does not require healing
        self.driver.execute_script("disable_healing_false")
```

### 3. Run test
To run tests in terminal with pytest you need to go to execute next commands:

```sh
python3 -m venv env
```

```sh
source ./env/bin/activate
```

```sh
python -m pip install -U pytest
```

```sh
python -m pip install -U selenium
```

```sh
pytest
```

> If you want to execute tests from specified file, please use the command: ```python -m pytest ./tests/test_css.py```
>> In case you want to run all tests in project use ```python -m pytest ./tests/``` command

### 4. Test Report
You can view the final test report at port 7878

```sh
http://localhost:7878
```
