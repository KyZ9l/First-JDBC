package com.dmdev.jdbc.starter.dao;

import com.dmdev.jdbc.starter.entity.Ticket;
import com.dmdev.util.ConectionManeger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDao {
    private static final TicketDao INSTANCE = new TicketDao();
    private static final String DELETE_SQL = """
             DELETE FROM ticket
             WHERE id = ?;
            """;
    private static final String SAVE_SQL = """
            INSERT INTO ticket( passenger_no, passenger_name, flight_id, seat_no, cost)
            VALUES (?,?,?,?,?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE ticket
            SET passenger_no =?,
                passenger_name=?,
                flight_id =?,
                seat_no=?,
                cost=?
                WHERE id = ?;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id, passenger_no, passenger_name, flight_id, seat_no, cost
            FROM ticket
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
         WHERE id = ?;
            """;



    private TicketDao() {
    }

    public List<Ticket> findAll()
    {
        try (var connection = ConectionManeger.get();
            var prepareStatement = connection.prepareStatement(FIND_ALL_SQL)){

            var resultSet = prepareStatement.executeQuery();
            List<Ticket> list  = new ArrayList<>();
            while (resultSet.next())
            {
                    list.add(buildTicket(resultSet));
            }
return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Ticket> findById(Long id) {
        try (var connection = ConectionManeger.get();) {
            var prepareStatement = connection.prepareStatement(FIND_BY_ID);
            prepareStatement.setLong(1, id);

            var resultSet = prepareStatement.executeQuery();
            Ticket ticket = null;
            if (resultSet.next()) {
                    ticket = buildTicket(resultSet);
            }
            return Optional.ofNullable(ticket);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Ticket buildTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
                resultSet.getLong("flight_id"),
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("cost")
        );
    }


    public void update(Ticket ticket) {
        try (var connection = ConectionManeger.get();
             var prepareStatement = connection.prepareStatement(UPDATE_SQL)) {

            prepareStatement.setString(1, ticket.getPassengerNo());
            prepareStatement.setString(2, ticket.getPassengerName());
            prepareStatement.setLong(3, ticket.getFlightId());
            prepareStatement.setString(4, ticket.getSeatNo());
            prepareStatement.setBigDecimal(5, ticket.getCost());
            prepareStatement.setLong(6, ticket.getId());

            prepareStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Ticket save(Ticket ticket) {
        try (var connection = ConectionManeger.get();
             var prepareStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setString(1, ticket.getPassengerNo());
            prepareStatement.setString(2, ticket.getPassengerName());
            prepareStatement.setLong(3, ticket.getFlightId());
            prepareStatement.setString(4, ticket.getSeatNo());
            prepareStatement.setBigDecimal(5, ticket.getCost());

            prepareStatement.executeUpdate();

            var generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }
            return ticket;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean delete(Long id) {
        try (var connection = ConectionManeger.get();) {
            var prepareStatement = connection.prepareStatement(DELETE_SQL);
            prepareStatement.setLong(1, id);
            var i = prepareStatement.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }
}
