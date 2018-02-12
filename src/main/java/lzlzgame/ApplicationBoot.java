package lzlzgame;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("lzlzgame.dao.mapper")
public class ApplicationBoot {

	public static void main(String[] args) {
        SpringApplication.run(ApplicationBoot.class, args);
	}
}
