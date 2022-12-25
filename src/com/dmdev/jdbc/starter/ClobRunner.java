package com.dmdev.jdbc.starter;

import com.dmdev.util.ConectionManeger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobRunner {
    public static void main(String[] args) throws SQLException, IOException {

getImage();



    }

    private static void getImage() throws SQLException, IOException {

        String sql = """
            SELECT image
            FROM aircraft
            WHERE id = ?;
            """;

        try (Connection connection = ConectionManeger.get();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
                prepareStatement.setInt(1,1);
            ResultSet resultSet = prepareStatement.executeQuery();
            while (resultSet.next())
            {
                byte[] bytes = resultSet.getBytes("image");
                Files.write(Path.of("resources","newBoing.jpg"),bytes);
            }


        }

    }


    public static void setImadge() throws SQLException, IOException {

        String sql = """
                UPDATE aircraft
                SET image = ?
                WHERE id = 1;   
                """;

        try (Connection connection = ConectionManeger.get();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setBytes(1, Files.readAllBytes(Path.of("resources","boeing737.jpg")));
            prepareStatement.executeUpdate();

        }




    }

}
