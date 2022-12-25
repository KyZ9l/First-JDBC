package com.dmdev.jdbc.starter;

import com.dmdev.util.ConectionManeger;

import java.sql.*;
import java.util.Arrays;

public class TransaktionRunner {
    public static void main(String[] args) throws SQLException {

        Connection connection = null;
        Statement statement1 = null;




        long id = 8;
        String sql1 = "DELETE FROM flight WHERE id = " + id;
        String sql2 = "DELETE FROM ticket WHERE flight_id = " + id;


        try {

            connection = ConectionManeger.get();
            connection.setAutoCommit(false);
            statement1 = connection.createStatement();

            statement1.addBatch(sql2);
            statement1.addBatch(sql1);

            int[] batch = statement1.executeBatch();

            System.out.println(Arrays.toString(batch));

            if(true)
            {
                throw new RuntimeException("oops");
            }

            connection.commit();

        } catch (Exception e) {
            if(connection != null)
                connection.rollback();
            throw e;
        }finally {
            if(connection!= null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(statement1!= null)
            statement1.close();
        }


    }
}
