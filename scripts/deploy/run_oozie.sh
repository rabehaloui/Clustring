#!/usr/bin/env bash

oozie job --oozie {{ server_oozie }} \
-config {{ application_path }}/conf/application.properties \
-D oozie.coord.application.path={{oozieApplicationCPath}} -run