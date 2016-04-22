package com.x.agile.px.bo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.DeclarationConstants;
import com.agile.api.IAgileClass;
import com.agile.api.IAttribute;
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
		String attrExemBaseID = prop.getProperty("DECLARATION_MFR_ATTR_EXEMPTION_BASE_ID");
		String attrDecCompBaseID = prop.getProperty("DECLARATION_MFR_ATTR_DEC_COMP_BASE_ID");
		// Object [] attvalArr = new Object[attrBaseIDArr.length];
		try {
			ITable updTable = dataObj.getTable(Integer.parseInt(prop.getProperty("FILLDOWN_TABLE_BASE_ID")));
			// IAgileClass cls = dataObj.getAgileClass();
			// IAttribute attr =
			// cls.getAttribute(Integer.parseInt(prop.getProperty("TAB_SORT_ORDER_ATTR_ID")));
			// Specify the sort attribute for the table iterator
			// ITable.ISortBy sortByNumber = updTable.createSortBy(attr,
			// ITable.ISortBy.Order.ASCENDING);
			// Create a sorted table iterator
			// ITwoWayIterator updItr = updTable.getTableIterator(new
			// ITable.ISortBy[] {sortByNumber});

			ITwoWayIterator updItr = updTable.getTableIterator();
			IRow updTabRow = null;
			Object attrDecCompVal = null;
			Object attrExempVal = null;

			while (updItr.hasNext()) {
				updTabRow = (IRow) updItr.next();
				attrDecCompVal = updTabRow.getValue(Integer.parseInt(attrDecCompBaseID));
				attrExempVal = updTabRow.getValue(Integer.parseInt(attrExemBaseID));
				if ((attrDecCompVal != null && !attrDecCompVal.toString().isEmpty())
						|| (attrExempVal != null && !attrExempVal.toString().isEmpty())) {
					break;
				}
			}

			if ((attrDecCompVal != null && !attrDecCompVal.toString().isEmpty())
					|| (attrExempVal != null && !attrExempVal.toString().isEmpty())) {
				// ICell cellObjDecComp = null;
				// ICell cellObjExem = null;
				updItr = updTable.getTableIterator();
				updTabRow = null;
				Map valMap = null;
				while (updItr.hasNext()) {
					updTabRow = (IRow) updItr.next();
					valMap = new HashMap();
					if (attrExempVal != null)
						valMap.put(Integer.parseInt(attrExemBaseID), attrExempVal);
					if (attrDecCompVal != null)
						valMap.put(Integer.parseInt(attrDecCompBaseID), attrDecCompVal);
					// cellObjExem =
					// updTabRow.getCell(Integer.parseInt(attrExemBaseID));
					// updTabRow.setValue(Integer.parseInt(attrExemBaseID),attrExempVal);
					// updTabRow.setValue(Integer.parseInt(attrDecCompBaseID),attrDecCompVal);
					updTabRow.setValues(valMap);
					/*
					 * if (cellObjExem!=null) {
					 * cellObjExem.setValue(attrExempVal); }
					 */
					// cellObjDecComp =
					// updTabRow.getCell(Integer.parseInt(attrDecCompBaseID));
					/*
					 * if (cellObjDecComp!=null) {
					 * cellObjDecComp.setValue(attrDecCompVal); }
					 */
				}
			} else {
				msg = prop.getProperty("ATTR_VALUE_EMPTY_MESSAGE");
			}

		} catch (NumberFormatException e) {
			logger.error(e.getMessage(), e);
			msg = e.getMessage();
		}
		return msg;
	}

}
