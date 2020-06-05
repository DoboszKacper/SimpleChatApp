package jms1.cmdChat;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineChat {

    public static void main(String[] args) throws NamingException, JMSException, IOException {
        System.out.print("Podaj swoj nick: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String userName = reader.readLine();


            Context ctx = new InitialContext();
            ConnectionFactory fact = (ConnectionFactory) ctx.lookup("ConnectionFactory");
            String admDestName = "topic1";
            Destination dest = (Destination) ctx.lookup(admDestName);
            Connection con = fact.createConnection();

            Subscribe(con,dest);
            Publish(con,dest,userName);

    }
    public static void Subscribe(Connection con, Destination dest) throws JMSException {
        Session ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer receiver = ses.createConsumer(dest);
        receiver.setMessageListener(new SampleListener());
    }
    public static void Publish(Connection con, Destination dest, String username) throws JMSException, IOException {
        Session ses = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = ses.createProducer(dest);
        con.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String messageToSend = reader.readLine();
            if(messageToSend.equalsIgnoreCase("exit")){
                con.close();
                System.exit(0);
            }else{
                TextMessage msg = ses.createTextMessage();
                msg.setText("["+username+"]: "+messageToSend);
                producer.send(msg);
            }
        }

    }

}
