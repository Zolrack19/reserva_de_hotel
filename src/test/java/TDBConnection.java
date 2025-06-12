import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import com.example.hotel.util.HibernateUtil;

public class TDBConnection {
  
  @Test
  public void testConexionDB() {
    Session s = HibernateUtil.getSession().openSession();
    s.close();
  }
}
