/**
 * 
 */
package com.zdjray.irent.biz.beans;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author zdjrayzhang
 *
 */
public class RentInfo implements Serializable {

	private static final long serialVersionUID = 94051210643643388L;
	private Calendar rentStartDate; // 租房开始时间
	private Calendar rentEndDate; // 租房结束时间
	private int rentPrice; // 总租金
	private int rentDeposit; // 定金
	
	public RentInfo() {
		this(Calendar.getInstance(), Calendar.getInstance(), 5000, 0);
	}
	public RentInfo(Calendar rentStartDate, Calendar rentEndDate, int rentPrice, int rentDeposit) {
		super();
		this.rentStartDate = rentStartDate;
		this.rentEndDate = rentEndDate;
		this.rentPrice = rentPrice;
		this.rentDeposit = rentDeposit;
	}
	public Calendar getRentStartDate() {
		return rentStartDate;
	}
	public void setRentStartDate(Calendar rentStartDate) {
		this.rentStartDate = rentStartDate;
	}
	public Calendar getRentEndDate() {
		return rentEndDate;
	}
	public void setRentEndDate(Calendar rentEndDate) {
		this.rentEndDate = rentEndDate;
	}
	public int getRentPrice() {
		return rentPrice;
	}
	public void setRentPrice(int rentPrice) {
		this.rentPrice = rentPrice;
	}
	public int getRentDeposit() {
		return rentDeposit;
	}
	public void setRentDeposit(int rentDeposit) {
		this.rentDeposit = rentDeposit;
	}
	@Override
	public String toString() {
		return "RentInfo [rentStartDate=" + rentStartDate + ", rentEndDate="
				+ rentEndDate + ", rentPrice=" + rentPrice + ", rentDeposit="
				+ rentDeposit + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rentDeposit;
		result = prime * result
				+ ((rentEndDate == null) ? 0 : rentEndDate.hashCode());
		result = prime * result + rentPrice;
		result = prime * result
				+ ((rentStartDate == null) ? 0 : rentStartDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RentInfo other = (RentInfo) obj;
		if (rentDeposit != other.rentDeposit)
			return false;
		if (rentEndDate == null) {
			if (other.rentEndDate != null)
				return false;
		} else if (!rentEndDate.equals(other.rentEndDate))
			return false;
		if (rentPrice != other.rentPrice)
			return false;
		if (rentStartDate == null) {
			if (other.rentStartDate != null)
				return false;
		} else if (!rentStartDate.equals(other.rentStartDate))
			return false;
		return true;
	}
}
