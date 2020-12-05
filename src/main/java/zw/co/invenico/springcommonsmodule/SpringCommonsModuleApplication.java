package zw.co.invenico.springcommonsmodule;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableFeignClients
@Configuration
@ComponentScan(basePackages = "zw.co.invenico.springcommonsmodule")
@EnableJpaAuditing
public class SpringCommonsModuleApplication {

}
