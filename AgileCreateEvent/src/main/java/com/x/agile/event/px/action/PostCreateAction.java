package com.x.agile.event.px.action;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.agile.api.APIException;
import com.agile.api.IAgileSession;
import com.agile.api.IDataObject;
import com.agile.api.IItem;
import com.agile.api.INode;
import com.agile.px.ActionResult;
import com.agile.px.EventActionResult;
import com.agile.px.IEventAction;
import com.agile.px.IEventInfo;
import com.agile.px.IObjectEventInfo;
import com.x.agile.event.px.bo.CreateEventBO;


/**
 * @author 
 * Description: CHnage status on work flow, Post Event Action Class 
 *
 */
public class PostCreateAction implements IEventAction {

	public static void main(String args[]){
		CreateEventBO updTabObj = new CreateEventBO();
		try {
			Logger logger = Logger.getLogger(PostCreateAction.class);
			logger.info("Logger done");
			IAgileSession session = updTabObj.getAgileSession();
			updTabObj.init();
			IDataObject dataObj = (IDataObject)session.getObject(IItem.OBJECT_TYPE, "");
			updTabObj.updateDefaultAttr(dataObj);
		}
		catch(APIException e){
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public EventActionResult doAction(IAgileSession session, INode node1, IEventInfo eventInfo) {
		EventActionResult eveActRes = null;
		ActionResult actRes = null;
		//PropertyConfigurator.configure(Utils.loadPropertyFile(System.getenv("AGILE_PROPERTIES")+"\\ExportFTPAgileDatalog4j.properties"));
		Logger logger = Logger.getLogger(PostCreateAction.class);
		IObjectEventInfo eventInfoObj = (IObjectEventInfo) eventInfo;
		
		logger.info("PostCreateAction Started...");
		try {
			IDataObject dataObj = eventInfoObj.getDataObject();
			logger.info("PostCreate Object: "+dataObj);
			CreateEventBO createBOObj = new CreateEventBO();
			createBOObj.init();
			createBOObj.updateDefaultAttr(dataObj);
			actRes = new ActionResult(ActionResult.STRING, "Defult values set Successfully!");
			logger.info("PostCreateAction Completed Successfully");
		} catch (APIException e) {
			logger.error(e.getMessage(), e);
			actRes = new ActionResult(ActionResult.EXCEPTION, e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			actRes = new ActionResult(ActionResult.EXCEPTION, e);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			actRes = new ActionResult(ActionResult.EXCEPTION, e);
		}
		eveActRes = new EventActionResult(eventInfoObj, actRes);
		return eveActRes;
	}

}
