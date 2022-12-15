package ca.jrvs.apps.jdbc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DataAccessObject <T extends DataTransferObject> {
  protected final Connection connection;
  protected final static String LAST_VAL = "SELECT last_value FROM ";
  protected final static String CUSTOMER_SEQUENCE = "hp_customer_seq";
  static Logger logger = LoggerFactory.getLogger(DataAccessObject.class);

  public DataAccessObject(Connection connection){
    super();
    this.connection = connection;
  }

  public abstract T findById(long id);
  public abstract List<T> findAll();
  public abstract T update(T dto);
  public abstract T create(T dto);
  public abstract void delete(long id);

  protected int getLastVal(String sequence){
    int key = 0;
    String sql = LAST_VAL + sequence;
    try(Statement statement = connection.createStatement()){
      ResultSet rs = statement.executeQuery(sql);
      while(rs.next()){
        key=rs.getInt(1);
      }
      return key;
    }catch (SQLException e ){
      logger.error("Problem connecting to database.", e);
      throw new RuntimeException(e);
    }
  }
}


