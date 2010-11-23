#!/bin/tcsh -v
#
# Copyright (C) 2010 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# This file looks like:
cat << EOF
<deployment name='defaultClientConfig'
    xmlns='http://xml.apache.org/axis/wsdd/'
    xmlns:java='http://xml.apache.org/axis/wsdd/providers/java'>
  <globalConfiguration>
    <parameter name='disablePrettyXML' value='true'/>
  </globalConfiguration>
  <!-- <transport name='http' pivot='java:com.google.api.adwords.lib.AdWordsHttpSender' /> -->
  <transport name='http' pivot='java:com.penumbrous.adwordsapi.lib.AdWordsGaeHttpHandler' />
  <transport name='local' pivot='java:org.apache.axis.transport.local.LocalSender' />
  <transport name='java' pivot='java:org.apache.axis.transport.java.JavaSender' />
</deployment>
EOF

echo "Copying client-config.wsdd"
cp /usr/local/src/penumbrous/src/java/com/penumbrous/adwordsapi/prototypes/client-config.wsdd  /usr/local/src/penumbrous/out/production/penumbrous/client-config.wsdd

echo "Running sandbox initialization"
/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Home/bin/java -Dfile.encoding=MacRoman -classpath /System/Library/Frameworks/JavaVM.framework/Versions/A/Resources/Deploy.bundle/Contents/Resources/Java/deploy.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/dt.jar:/System/Library/Frameworks/JavaVM.framework/Versions/A/Resources/Deploy.bundle/Contents/Resources/Java/javaws.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/jce.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/management-agent.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/lib/sa-jdi.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/alt-rt.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/charsets.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/classes.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/jconsole.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/jsse.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/laf.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Classes/ui.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/lib/ext/apple_provider.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/lib/ext/dnsns.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/lib/ext/localedata.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/lib/ext/sunjce_provider.jar:/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/lib/ext/sunpkcs11.jar:/usr/local/src/penumbrous/out/production/penumbrous:/usr/local/src/penumbrous/lib:/usr/local/src/penumbrous/lib/saaj.jar:/usr/local/src/penumbrous/lib/commons-logging-1.0.4.jar:/usr/local/src/penumbrous/lib/commons-discovery-0.2.jar:/usr/local/src/penumbrous/lib/commons-httpclient-3.1.jar:/usr/local/src/penumbrous/lib/commons-lang-2.4.jar:/usr/local/src/penumbrous/lib/log4j-1.2.15.jar:/usr/local/src/penumbrous/lib/wsdl4j-1.5.1.jar:/usr/local/src/penumbrous/lib/commons-codec-1.3.jar:/usr/local/src/penumbrous/lib/opencsv-1.8.jar:/usr/local/src/penumbrous/lib/axis.jar:/usr/local/src/penumbrous/lib/adwords-api-8.1.0-loner.jar:/usr/local/src/penumbrous/lib/jaxrpc.jar:/usr/local/src/penumbrous/lib/guava-r06.jar:/usr/local/src/penumbrous/lib/jsr-305.jar:/usr/local/src/penumbrous/lib/guice-snapshot-20100909.jar:/usr/local/src/penumbrous/build_lib:/usr/local/src/penumbrous/build_lib/junit-4.8.2.jar:/usr/local/src/penumbrous/build_lib/axis-1.4-src.jar:/usr/local/src/penumbrous/build_lib/guava-src-r06.zip:/usr/local/src/penumbrous/build_lib/jsr-305-src.jar:/usr/local/src/appengine-java-sdk/lib/agent:/usr/local/src/appengine-java-sdk/lib/impl:/usr/local/src/appengine-java-sdk/lib/shared:/usr/local/src/appengine-java-sdk/lib/testing:/usr/local/src/appengine-java-sdk/lib/user:/usr/local/src/appengine-java-sdk/lib:/usr/local/src/appengine-java-sdk/lib/agent/appengine-agent.jar:/usr/local/src/appengine-java-sdk/lib/agent/appengine-agentimpl.jar:/usr/local/src/appengine-java-sdk/lib/impl/appengine-api-labs.jar:/usr/local/src/appengine-java-sdk/lib/impl/appengine-api-stubs.jar:/usr/local/src/appengine-java-sdk/lib/impl/appengine-api.jar:/usr/local/src/appengine-java-sdk/lib/impl/appengine-local-runtime.jar:/usr/local/src/appengine-java-sdk/lib/shared/appengine-local-runtime-shared.jar:/usr/local/src/appengine-java-sdk/lib/shared/geronimo-el_1.0_spec-1.0.1.jar:/usr/local/src/appengine-java-sdk/lib/shared/geronimo-jsp_2.1_spec-1.0.1.jar:/usr/local/src/appengine-java-sdk/lib/shared/geronimo-servlet_2.5_spec-1.2.jar:/usr/local/src/appengine-java-sdk/lib/testing/appengine-testing.jar:/usr/local/src/appengine-java-sdk/lib/user/appengine-api-1.0-sdk-1.3.5.jar:/usr/local/src/appengine-java-sdk/lib/user/appengine-api-labs-1.3.5.jar:/usr/local/src/appengine-java-sdk/lib/user/appengine-jsr107cache-1.3.5.jar:/usr/local/src/appengine-java-sdk/lib/user/jsr107cache-1.1.jar:/usr/local/src/appengine-java-sdk/lib/appengine-tools-api.jar com.penumbrous.adwordsapi.prototypes.SandboxInitialization
