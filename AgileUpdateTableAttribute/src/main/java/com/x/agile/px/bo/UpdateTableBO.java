package com.x.agile.px.bo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.ICell;
import com.agile.api.IDataObject;
import com.agile.api.IRow;
import com.agile.api.ITable;
import com.agile.api.ITwoWayIterator;

/**
 * Description: Process Extension class holds implemented business logic
 *
 */
public class UpdateTableBO {

	Properties prop;
	final static Logger logger = Logger.getLogger(UpdateTableBO.class);

	public void init() throws IOException {

		InputStream inputStream = null;
		prop = new Properties();
		String propFileName = "config.properties";
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
	 *             purpose: Reads Affected Item Table of the Change and update
	 *             attributes of affected items
	 */
	public String updateTableAttr(IDataObject dataObj) throws APIException {
		String msg = "";
		String attrBaseID = prop.getProperty("FILLDOWN_ATTR_BASE_ID");
		try {
			ITable updTable = dataObj.getTable(Integer.parseInt(prop.getProperty("FILLDOWN_TABLE_BASE_ID")));
			ITwoWayIterator updItr = updTable.getTableIterator();
			IRow updTabRow = null;
			String attrVal = "";
			while (updItr.hasNext()) {
				updTabRow = (IRow) updItr.next();
				attrVal = updTabRow.getValue(Integer.parseInt(attrBaseID)).toString();
				if (!attrVal.isEmpty()) {
					break;
				}
			}
			if (!attrVal.isEmpty()) {
				ICell cellObj = null;
				updItr = updTable.getTableIterator();
				updTabRow = null;
				while (updItr.hasNext()) {
					updTabRow = (IRow) updItr.next();
					cellObj = updTabRow.getCell(Integer.parseInt(attrBaseID));
					if (cellObj!=null) {
						cellObj.setValue(attrVal);
					}
				}
			}else{
				msg = prop.getProperty("ATTR_VALUE_EMPTY_MESSAGE");
			}
			
		} catch (NumberFormatException e) {
			logger.error(e.getMessage(), e);
			msg = e.getMessage();
		}
		return msg;
	}

}
