package com.x.agile.event.px.bo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.AgileSessionFactory;
import com.agile.api.IAgileSession;
import com.agile.api.IDataObject;

/**
 * Description: Process Extension class holds implemented business logic
 *
 */
public class CreateEventBO {

	Properties prop;
	final static Logger logger = Logger.getLogger(CreateEventBO.class);

	public void init() throws IOException {

		InputStream inputStream = null;
		prop = new Properties();
		//String propFileName = "config.properties";
		
		String propFileName =System.getenv("ConflictMineralRollup")+"/AgileCreateEvent.properties";
		
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
				logger.info("Prop file loaded.");
			} else {
				throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	/**
	 * @param aglSesion
	 * @param chgObj
	 * @throws APIException
	 *             purpose: Set Default values form property file for given subclass while Create
	 */
	public void updateDefaultAttr(IDataObject dataObj) throws APIException {
		String subClassName = dataObj.getAgileClass().getName();
		String attrList = prop.getProperty(subClassName.replace(" ", "_")+"_ATTR_LIST");
		if(attrList!=null){
			Map<Object,Object> valMap = new HashMap<Object, Object>();
			for(String attr : attrList.split(";")){
				if(!attr.isEmpty()){
				try {
					valMap.put(Integer.parseInt(attr.split(":")[0]), attr.split(":")[1]);
				} catch (NumberFormatException e) {
					valMap.put(attr.split(":")[0], attr.split(":")[1]);
				}
			}
			}
			dataObj.setValues(valMap);
		}
	}
	
	public IAgileSession getAgileSession() throws APIException
	 {
			HashMap<Integer, String> params = new HashMap<Integer, String>();
			params.put(AgileSessionFactory.USERNAME, prop.getProperty("AGL_USER"));
			params.put(AgileSessionFactory.PASSWORD, prop.getProperty("AGL_PWD"));
			params.put(AgileSessionFactory.URL, prop.getProperty("AGL_URL"));

			IAgileSession session = AgileSessionFactory.createSessionEx(params);

			logger.info("Connected to Agile!!!");

			return session;
		}

}
