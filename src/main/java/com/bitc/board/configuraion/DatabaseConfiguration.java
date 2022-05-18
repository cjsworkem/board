package com.bitc.board.configuraion;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

// @Configuration : 해당 클래스가 프로젝트 설정 파일임을 나타내는 어노테이션
@Configuration
// @PropertySource : 해당 클래스에서 사용할 설정 내용을 지정한 파일에서 불러와서 사용할 수 있음
@PropertySource("classpath:/application.properties")
public class DatabaseConfiguration {
    @Autowired
    private ApplicationContext appContext;

    @Bean
// @ConfigurationProperties : @PropertySource 어노테이션을 통해서 지정한 파일 내에 있는 설정 중 prefix로 지정한 설정을 가져옴
//    application.properties 폴더 안에있는 spring.datasource.hikari로 시작하는 설정을 가져와서 사용하도록 지정!
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

//  위에서 생성한 히카리cp의 설정 파일을 이용하여 데이터 베이스와 연결하는 데이터소스를 생성
    @Bean
    public DataSource dataSource() throws Exception{
        DataSource dataSource = new HikariDataSource(hikariConfig());
        System.out.println(dataSource.toString());
        return dataSource;
    }

    
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setDataSource(dataSource);
//  ORM인 Mybatis를 사용하여 xml 파일의 내용을 불러옴
//  sql 쿼리문이 입력되어있는 xml 파일의 위치를 지정함
//  ** 모든파일,모든 폴더를 뜻한 (폴더 단계에 관계없이 지정한 파일형식대로 검색하여 사용하겠다는 의미)
//  sql-*.xml 은 접두사가 sql- 이고, 접미사는 .xml인 모든 파일을 검색하여 사용
        ssfb.setMapperLocations(appContext.getResources("classpath:/sql/**/sql-*.xml"));
        ssfb.setConfiguration(mybatisConfig());
        return ssfb.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory ssf) {
        return new SqlSessionTemplate(ssf);
    }

//    application.properties에 설정된 마이바티스 관련 설정을 불러와서 사용
//    카멜명명법을 사용한다는 내용을 로드함
    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration mybatisConfig(){
        return new org.apache.ibatis.session.Configuration();
    }

}
