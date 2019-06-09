package dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import to.CarInfoTo;

public class CarInfoDao {
	private String errormessage;

	public String getErrormessage() {
		return errormessage;
	}

	public boolean insertRecord(CarInfoTo record) {
		try {
			String query = "insert into carinfo (car_number,arrival_date,arrival_time) values(?,sysdate(),curtime())";
			PreparedStatement stmt = DataConnection.getStatement(query);
			stmt.setString(1, record.getCar_number());
			boolean result = stmt.executeUpdate() > 0;
			stmt.close();
			return result;
		} catch (Exception ex) {
			errormessage = ex.toString();
			return false;
		}
	}
	public boolean updateRecord(CarInfoTo record){
		try{
			String query="update carinfo set car_number=?,arrival_date=?,arrival_time=? where car_id=?";
			PreparedStatement stmt=DataConnection.getStatement(query);
			stmt.setString(1, record.getCar_number());
			stmt.setDate(2, record.getArrival_date());
			stmt.setTime(3, record.getArrival_time());
			stmt.setInt(4, record.getCar_id());
			boolean result=stmt.executeUpdate()>0;
			stmt.close();
			return result;
		}catch(Exception ex){
			errormessage=ex.toString();
			return false;
		}
	}
	public boolean deleteRecord(int carid){
		try{
			String query="delete from carinfo where car_id=?";
			PreparedStatement stmt=DataConnection.getStatement(query);
			stmt.setInt(1, carid);
			boolean result=stmt.executeUpdate()>0;
			stmt.close();
			return result;
		}catch(Exception ex){
			errormessage=ex.toString();
			return false;
		}
	}
	public CarInfoTo getRecord(int carid){
		try{
			String query="select car_id,car_number,arrival_date,arrival_time from carinfo where car_id=?";
			PreparedStatement stmt=DataConnection.getStatement(query);
			stmt.setInt(1, carid);
			CarInfoTo result=null;
			ResultSet rs=stmt.executeQuery();
			if(rs.next()){
				result=new CarInfoTo();
				result.setCar_id(rs.getInt("car_id"));
				result.setCar_number(rs.getString("car_number"));
				result.setArrival_date(rs.getDate("arrival_date"));
				result.setArrival_time(rs.getTime("arrival_time"));
			}
			stmt.close();
			return result;
		}catch(Exception ex){
			errormessage=ex.toString();
			return null;
		}
	}
	public List<CarInfoTo> getAllRecord(){
		try{
			String query="select * from carinfo";
			PreparedStatement stmt=DataConnection.getStatement(query);
			List<CarInfoTo> result=null;
			ResultSet rs=stmt.executeQuery();
			if(rs.next()){
				result=new ArrayList<CarInfoTo>();
				do{
					CarInfoTo record=new CarInfoTo();
					record.setCar_id(rs.getInt("car_id"));
					record.setCar_number(rs.getString("car_number"));
					record.setArrival_date(rs.getDate("arrival_date"));
					record.setArrival_time(rs.getTime("arrival_time"));
					result.add(record);
				}while(rs.next());
			}
			stmt.close();
			return result;
		}catch(Exception ex){
			errormessage=ex.toString();
			return null;
		}
	}
}
