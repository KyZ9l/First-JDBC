package com.dmdev.jdbc.starter;


import com.dmdev.util.ConectionManeger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class JdbcRunner {



    public static void main(String[] args) throws SQLException {

        try {

            metaDataConection();
        }finally {

            ConectionManeger.closePool();
        }



    }


    public static void metaDataConection()
    {

        try (Connection connection = ConectionManeger.get()) {
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println("getPool");
            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next())
            {
                String catalog = catalogs.getString(1);
                ResultSet schemas = metaData.getSchemas();
                while (schemas.next())
                {
                    String schem = schemas.getString("TABLE_SCHEM");

                    ResultSet tables = metaData.getTables(catalog, schem, "%", new String[] {"TABLE"});
                    while (tables.next())
                    {
                        if(schem.equals("public"))
                        {
                            while (tables.next())
                            {
                                System.out.println(tables.getString("TABLE_NAME"));
                            }
                        }
                    }
                }


            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static List<Long> getFlightId (LocalDateTime t1, LocalDateTime t2) throws SQLException {

        String sql = """
                SELECT 
                id
                FROM flight
                WHERE departure_date BETWEEN ? and ?
                """;

        List<Long> list = new ArrayList<>();

        try (Connection connection = ConectionManeger.get();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setFetchSize(50);
            prepareStatement.setQueryTimeout(10);
            prepareStatement.setMaxRows(100);

            System.out.println(prepareStatement);
            prepareStatement.setTimestamp(1, Timestamp.valueOf(t1));
            System.out.println(prepareStatement);
            prepareStatement.setTimestamp(2,Timestamp.valueOf(t2));
            System.out.println(prepareStatement);

            ResultSet resultSet = prepareStatement.executeQuery();

            while (resultSet.next())
            {
                list.add(resultSet.getLong("id"));
            }


        }

return list;
    }

}




