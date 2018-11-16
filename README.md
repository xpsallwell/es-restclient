# es-restclient
对elasticsearch-rest-client二次开发工具类，新增加freemarker的支持。可以方便的结合spring使用。

配置示例：
 ES搜索引擎配置 
 #elasticsearch
elasticsearch.hosts=172.16.103.13:9200;172.16.103.16:9200
elasticsearch.connectTimeout=6000

    <bean id="restClientConfig" class="com.xps.es.restclient.RestClientConfig" >
         <constructor-arg index="0">
             <props>
                 <prop key="es-hosts">${elasticsearch.hosts}</prop>
                 <prop key="es-connectTimeout">${elasticsearch.connectTimeout}</prop>
             </props>
         </constructor-arg>
        <!--
        <property name="httpHostList">
            <list>
                <value>172.16.103.13:9200</value>
                <value>172.16.103.16:9200</value>
            </list>
        </property>
        <property name="connectTimeout" value="6000"></property>-->
    </bean>
    <bean id="restClient" class="com.xps.es.restclient.impl.ESRestClientImpl">
        <constructor-arg index="0" ref="restClientConfig"></constructor-arg>
    </bean>

    <bean id="reqJsonTemplate" class="com.xps.es.restclient.template.ReqJsonTemplateImpl">
        <constructor-arg index="0" value="classpath*:es-template/es-*.xml"></constructor-arg>
    </bean>
