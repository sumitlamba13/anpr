package to;

import java.sql.Date;
import java.sql.Time;

public class CarInfoTo {
	private int car_id;
	private String car_number;
	private Date arrival_date;
	private Time arrival_time;
	
	public int getCar_id(){
		return car_id;
	}
	public void setCar_id(int car_id){
		this.car_id=car_id;
	}
	public String getCar_number(){
		return car_number;
	}
	public void setCar_number(String car_number){
		this.car_number=car_number;
	}
	public Date getArrival_date(){
		return arrival_date;
	}
	public void setArrival_date(Date arrival_date){
		this.arrival_date=arrival_date;
	}
	public Time getArrival_time(){
		return arrival_time;
	}
	public void setArrival_time(Time arrival_time){
		this.arrival_time=arrival_time;
	}
	
}
