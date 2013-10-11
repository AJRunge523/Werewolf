package edu.wm.werewolf.component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@Component
public class WerewolfAuthenticationProvider implements AuthenticationProvider {

	//@Autowired private MongoClient mongo;
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        DBCollection coll = getCollection("users");
        DBCursor cursor = coll.find(new BasicDBObject("username", name).append("password", password));
        if(cursor.hasNext()) {
            List<SimpleGrantedAuthority> grantedAuths = new ArrayList<SimpleGrantedAuthority>();
        	DBObject o = cursor.next();
        	System.out.println(o.get("role"));
        	if(o.get("role").toString().equals("ADMIN"))
        	{
        		System.out.println("ajdsoifjsodijosfjdlf");
        		grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        	}
        	else
        		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
            return auth;
        } else {
            return null;
        }
	}

	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

	private DBCollection getCollection(String collection)
	{
		DB db;
		DBCollection coll = null;
		/*
		if(mongo == null)
		{
			try {
				mongo = new MongoClient("localhost", 27017);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		db = mongo.getDB("werewolf");
		coll = db.getCollection(collection);
		*/
		return coll;
	}
}
