package com.selfservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

/**
 * @Author QuanCZS 2019/6/17 19:28
 */
@Service
public class SynicService {

    private static final Logger log = LoggerFactory.getLogger(SynicService.class);

    @Value("${insertOracle.className}")
    private String insertClassName;

    @Value("${insertOracle.url}")
    private String url;

    @Value("${insertOracle.username}")
    private String insertUsername;

    @Value("${insertOracle.password}")
    private String insertPassword;

    @Value("${deleteQLSX.className}")
    private String trunClassName;

    @Value("${deleteQLSX.url}")
    private String trunUrl;

    @Value("${deleteQLSX.username}")
    private String trunUsername;

    @Value("${deleteQLSX.password}")
    private String trunPassword;


    public void synic() throws SQLException {

        String truncateURL = "TRUNCATE TABLE QUESTION";
        String truncateURL1 = "TRUNCATE TABLE ACCEPT_ADDRESS";
        String truncateURL2 = "TRUNCATE TABLE CHARGE_ITEM";
        String truncateURL3 = "TRUNCATE TABLE COMMON_QUESTION";
        String truncateURL4 = "TRUNCATE TABLE MATERIAL_LIST";
        String truncateURL5 = "TRUNCATE TABLE MATERIALS";
        String truncateURL6 = "TRUNCATE TABLE MINIMUM_PARTICLE";

        String truncateURL7 = "TRUNCATE TABLE ATTACHMENT";

        String insertURL = "INSERT INTO QUESTION @Fyplatform  SELECT * FROM QUESTION";
        String insertURL1 = "INSERT INTO ACCEPT_ADDRESS @Fyplatform  SELECT * FROM ACCEPT_ADDRESS";
        String insertURL2 = "INSERT INTO CHARGE_ITEM @Fyplatform  SELECT * FROM CHARGE_ITEM";
        String insertURL3 = "INSERT INTO COMMON_QUESTION @Fyplatform  SELECT * FROM COMMON_QUESTION";
        String insertURL4 = "INSERT INTO MATERIAL_LIST @Fyplatform  SELECT * FROM MATERIAL_LIST";
        String insertURL5 = "INSERT INTO MATERIALS @Fyplatform  SELECT * FROM MATERIALS";
        String insertURL6 = "INSERT INTO MINIMUM_PARTICLE @Fyplatform  SELECT * FROM MINIMUM_PARTICLE";

        String insertURL7 = "INSERT INTO ATTACHMENT @Fyplatform  SELECT * FROM ATTACHMENT";

        Connection conn = null ;
        Connection conn2 = null ;
        Statement statement = null ;
        try {
            Class.forName(insertClassName);
            conn= DriverManager.getConnection(url,insertUsername,insertPassword);
            statement = conn.createStatement();

            //每次插入数据到199之前，先truncate掉199要同步的表
            try{
                Class.forName(trunClassName);
                conn2= DriverManager.getConnection(trunUrl,trunUsername,trunPassword);
                conn2.createStatement().executeQuery(truncateURL);
                conn2.createStatement().executeQuery(truncateURL1);
                conn2.createStatement().executeQuery(truncateURL2);
                conn2.createStatement().executeQuery(truncateURL3);
                conn2.createStatement().executeQuery(truncateURL4);
                conn2.createStatement().executeQuery(truncateURL5);
                conn2.createStatement().executeQuery(truncateURL6);
                conn2.createStatement().executeQuery(truncateURL7);
            }catch(Exception e){
                log.error("Truncate权力事项库相关表数据(199)出现异常，请检查连接...");
            }

            statement.executeQuery(insertURL);
            statement.executeQuery(insertURL1);
            statement.executeQuery(insertURL2);
            statement.executeQuery(insertURL3);
            statement.executeQuery(insertURL4);
            statement.executeQuery(insertURL5);
            statement.executeQuery(insertURL6);
            statement.executeQuery(insertURL7);

        } catch (Exception e) {
            log.error("同步表数据到199微信端出现异常，请检查连接...");
        }
        finally {
            conn.close();
            conn2.close();
            statement.close();
        }
    }

    public void synicQLSX() throws SQLException {

        String truncateURL = "TRUNCATE TABLE QLT_QLSX";
//        String truncateURL2 = "TRUNCATE TABLE ATTACHMENT";
        //


        String insertURL = "INSERT INTO QLT_QLSX @Fyplatform  SELECT * FROM QLT_QLSX";
//        String insertURL2 = "INSERT INTO ATTACHMENT @Fyplatform  SELECT * FROM ATTACHMENT";
        //


        Connection conn = null;
        Statement statement = null;
        Connection conn2 = null ;
        try {
            Class.forName(insertClassName);
            conn= DriverManager.getConnection(url,insertUsername,insertPassword);
            statement = conn.createStatement();
            //每次插入数据到199之前，先truncate掉199的权力事项表
            try{
                Class.forName(trunClassName);
                conn2= DriverManager.getConnection(trunUrl,trunUsername,trunPassword);
                conn2.createStatement().executeQuery(truncateURL);
//                conn2.createStatement().executeQuery(truncateURL2);
            }catch(Exception e){
                log.error("Truncate权力事项库表数据(199)出现异常，请检查连接...");
            }

            statement.executeQuery(insertURL);
//            statement.executeQuery(insertURL2);


        } catch (Exception e) {
            log.error("同步表数据到199微信端出现异常，请检查连接...");
        }
        finally {
            conn.close();
            conn2.close();
            statement.close();
        }
    }


}
