package com.niit.DAOImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.niit.DAO.UsersDetailDao;
import com.niit.Model.Cart;
import com.niit.Model.UserRole;
import com.niit.Model.Users;
import com.niit.Model.UsersDetail;

@Repository("usersDetailDAO")
public class UsersDetailDaoImpl implements UsersDetailDao{

    @Autowired
    private SessionFactory sessionFactory;
    
    @Transactional
    public void addUser(UsersDetail usersDetail) {
    	
        Session session = sessionFactory.openSession();     
        session.saveOrUpdate(usersDetail);      
        Users newUser = new Users();
        newUser.setUsername(usersDetail.getUsername());
        newUser.setPassword(usersDetail.getPassword());
        newUser.setEnabled(true);
        newUser.setUserId(usersDetail.getUserId());

        UserRole newUserRole = new UserRole();
        newUserRole.setUsername(usersDetail.getUsername());
        newUserRole.setRole("ROLE_USER");
        
        session.saveOrUpdate(newUser);
        session.saveOrUpdate(newUserRole);
        session.saveOrUpdate(usersDetail);
       
        Cart newCart = new Cart();
        newCart.setUsersDetail(usersDetail);
        usersDetail.setCart(newCart);
        session.saveOrUpdate(usersDetail);
        session.saveOrUpdate(newCart);
       
        session.flush();
    }

    
   //LOOK HERE
    @Transactional
    public boolean isValidUser(String name)
    {
    	//select * from UserDetails where id='101' and password='niit'
    	String hql="from UsersDetail where username='"+name+"'";
    	Query query=sessionFactory.openSession().createQuery(hql);
    	
    	List<UsersDetail> list = query.list();//list of users detail
    	
    	if(list!=null){
        	return false;//invalid user    		
    	}
    	else{
    		return true;//valid user
    	}
    }
    
    @Transactional
    public UsersDetail getUserById (int userId) {
        Session session = sessionFactory.openSession();
        return (UsersDetail) session.get(UsersDetail.class, userId);
    }
    
    @Transactional
    public List<UsersDetail> getAllUsers() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from UsersDetail");
        List<UsersDetail> usersDetail = query.list();
        return usersDetail;
    }
    
    @Transactional
    public UsersDetail getUserByUsername (String username) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from UsersDetail where username = ?");
        query.setString(0, username);
        return (UsersDetail) query.uniqueResult();
    }
}