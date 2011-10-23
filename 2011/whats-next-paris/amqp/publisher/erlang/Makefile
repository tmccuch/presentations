VERSION=2.6.1
DEPS=amqp_client rabbit_common

DEPS_DIR=deps
DEPS_APP_DIR=$(DEPS_DIR)/apps
EBIN_DIR=ebin
SRC_DIR=src

SOURCES=$(wildcard $(SRC_DIR)/*.erl)
TARGETS=$(patsubst $(SRC_DIR)/%.erl, $(EBIN_DIR)/%.beam, $(SOURCES))
DEP_TARGETS=$(patsubst %, $(DEPS_APP_DIR)/%, $(DEPS))

.PHONY: compile
compile: $(TARGETS)

.PHONY: run
run: compile
	ERL_LIBS=$(DEPS_APP_DIR) erl -pa ebin

$(EBIN_DIR)/%.beam : $(SRC_DIR)/%.erl $(DEPS_DIR)/.done | $(EBIN_DIR)
	ERL_LIBS=$(DEPS_APP_DIR) erlc -o ebin $<

$(DEPS_DIR)/.done: | $(DEPS_DIR) $(DEPS_APP_DIR)
	$(foreach DEP, $(DEPS), wget -O $(DEPS_DIR)/$(DEP).ez http://www.rabbitmq.com/releases/plugins/v$(VERSION)/$(DEP)-$(VERSION).ez && unzip -d $(DEPS_APP_DIR) $(DEPS_DIR)/$(DEP).ez;)

	touch $(DEPS_DIR)/.done

$(DEPS_APP_DIR): | $(DEPS_DIR)
	mkdir -p $(DEPS_APP_DIR)

$(DEPS_DIR):
	mkdir -p $(DEPS_DIR)

$(EBIN_DIR):
	mkdir -p $(EBIN_DIR)

.PHONY: clean
clean:
	rm -rf $(DEPS_DIR)
	rm -rf $(EBIN_DIR)
