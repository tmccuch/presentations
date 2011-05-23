PIP_CMD=pip-2.6
VIRTUAL_ENV_CMD=virtualenv-2.6
VIRTUAL_ENV_NAME=demo_env

.PHONY: clean
clean:
	rm -rf $(VIRTUAL_ENV_NAME)

.PHONY: run
run: $(VIRTUAL_ENV_NAME)
	$(VIRTUAL_ENV_NAME)/bin/python consumer.py

demo_env:
	$(VIRTUAL_ENV_CMD) $(VIRTUAL_ENV_NAME)
	$(PIP_CMD) -E $(VIRTUAL_ENV_NAME) install -e git+http://github.com/majek/puka.git#egg=puka