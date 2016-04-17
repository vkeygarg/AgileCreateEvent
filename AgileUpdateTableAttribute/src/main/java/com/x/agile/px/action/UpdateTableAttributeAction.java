package com.x.agile.px.action;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IDataObject;
import com.agile.api.INode;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;
import com.x.agile.px.bo.UpdateTableBO;

/**
 * @author 
 * Description: CHnage status on work flow, Post Event Action Class 
 *
 */
public class UpdateTableAttributeAction implements ICustomAction {

	public ActionResult doAction(IAgileSession aglSession, INode node, IDataObject dataObj) {
		ActionResult actRes = null;
		String msg = "";
		Logger logger = Logger.getLogger(UpdateTableAttributeAction.class);
		logger.info("UpdateTableAttributeAction Starts for Change ::"+dataObj);
		try {
			UpdateTableBO updTabObj = new UpdateTableBO();
			updTabObj.init();
			msg = updTabObj.updateTableAttr(dataObj);
			actRes = new ActionResult(ActionResult.STRING, msg.isEmpty() ? "Attributes updated Successfulyy!":msg);
			logger.info("UpdateTableAttributeAction Completed Successfully");
		} catch (APIException e) {
			logger.error(e.getMessage(), e);
			actRes = new ActionResult(ActionResult.EXCEPTION, e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			actRes = new ActionResult(ActionResult.EXCEPTION, e);
		}
		return actRes;
	}
	
	public static void main(String args[]){
		UpdateTableBO updTabObj = new UpdateTableBO();
		try {
			updTabObj.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
