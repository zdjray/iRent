/**
 * 
 */
package com.zdjray.irent.config;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.Set;

import com.zdjray.irent.biz.beans.RoomInfo;

/**
 * @author zdjrayzhang
 *
 */
public interface ConfigManager {

	//http://www.cnblogs.com/hanyonglu/archive/2012/03/01/2374894.html
	public Set<String> loadRoomInfoKeyList() throws OptionalDataException, ClassNotFoundException, IOException;
	
	public void saveRoomInfoKeyList(Set<String> roomInfoKeyList) throws IOException;
	
	public RoomInfo loadRoomInfo(String key) throws OptionalDataException, ClassNotFoundException, IOException;
	
	public void saveRoomInfo(String key, RoomInfo roomInfo) throws IOException;
	
	public void deleteRoomInfo(String key) throws IOException;
}
