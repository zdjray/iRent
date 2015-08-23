/**
 * 
 */
package com.zdjray.irent.biz.beans;

import java.io.Serializable;

/**
 * @author zdjrayzhang
 *
 */
public class RoomInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9053944815269909449L;
	private Contact contact;
	private RentInfo rentInfo;
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public RentInfo getRentInfo() {
		return rentInfo;
	}
	public void setRentInfo(RentInfo rentInfo) {
		this.rentInfo = rentInfo;
	}
	@Override
	public String toString() {
		return "RoomInfo [contact=" + contact + ", rentInfo=" + rentInfo + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contact == null) ? 0 : contact.hashCode());
		result = prime * result
				+ ((rentInfo == null) ? 0 : rentInfo.hashCode());
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
		RoomInfo other = (RoomInfo) obj;
		if (contact == null) {
			if (other.contact != null)
				return false;
		} else if (!contact.equals(other.contact))
			return false;
		if (rentInfo == null) {
			if (other.rentInfo != null)
				return false;
		} else if (!rentInfo.equals(other.rentInfo))
			return false;
		return true;
	}
}
