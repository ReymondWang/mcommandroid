package com.purplelight.mcommunity.constant;

import android.content.Context;

import com.purplelight.mcommunity.R;
import com.purplelight.mcommunity.util.Validation;

/**
 * 定义WebService需要的各种参数名称
 * Created by wangyn on 15/12/27.
 */
public class WebServiceAPI {

    public final static String NAMESPACE = "http://tempuri.org/";

    public static final int LOGIN = 0;
    public static final int GET_NOTICE_LIST = 1;
    public static final int GET_NOTICE = 2;
    public static final int GET_ATTACHMENT_FILE_LIST = 3;
    public static final int ADD_NOTICE_FEEDBACK = 4;
    public static final int ATTACH_FILE_EXISTS_CHECK = 5;
    public static final int GET_WORKFLOW_ACT_GROUP_NOT_SIGNED = 6;
    public static final int GET_WORKFLOW_ACT_GROUP_SIGNED = 7;
    public static final int GET_WORKFLOW_ACT_GROUP_DONE = 8;
    public static final int GET_WORKFLOW_ACT_LIST_NOT_SIGNED = 9;
    public static final int GET_WORKFLOW_ACT_LIST_SIGNED = 10;
    public static final int GET_WORKFLOW_ACT_LIST_DONE = 11;
    public static final int GET_WORKFLOW_PAGE = 12;
    public static final int GET_WORKFLOW_SEND_ROUTER = 13;
    public static final int GET_WORKFLOW_SEND_TASK_ACTOR = 14;
    public static final int GET_WORKFLOW_BACK_ROUTER = 15;
    public static final int GET_WORKFLOW_BACK_TASK_ACTOR = 16;
    public static final int PDF_FILE_COPY = 17;
    public static final int PDF_FILE_EXISTS_CHECK = 18;
    public static final int PDF_FILE_REMOVE = 19;
    public static final int PUSH_SEND = 20;
    public static final int WORKFLOW_BACK = 21;
    public static final int WORKFLOW_FINISH = 22;
    public static final int WORKFLOW_SEND = 23;
    public static final int WORKFLOW_SIGNIN = 24;
    public static final int WORKFLOW_TASK_FINISH = 25;
    public static final int PDF_FILE_FOLDER_CHECK = 26;
    public static final int VERSION_PATH = 27;
    public static final int UPDATE_PATH = 28;
    public static final int UPLOAD_IMAGE = 29;

    public static String getMethodName(int service){
        String mName = "";

        switch (service){
            case LOGIN:
                mName = "Login";
                break;
            case GET_NOTICE_LIST:
                mName = "GetNoticeList";
                break;
            case GET_NOTICE:
                mName = "GetNotice";
                break;
            case GET_ATTACHMENT_FILE_LIST:
                mName = "GetAttachmentFileList";
                break;
            case ADD_NOTICE_FEEDBACK:
                mName = "AddNoticeFeedBack";
                break;
            case ATTACH_FILE_EXISTS_CHECK:
                mName = "AttachFileExistsCheck";
                break;
            case GET_WORKFLOW_ACT_GROUP_NOT_SIGNED:
                mName = "GetWorkFlowActGroupNotSigned";
                break;
            case GET_WORKFLOW_ACT_GROUP_SIGNED:
                mName = "GetWorkFlowActGroupSigned";
                break;
            case GET_WORKFLOW_ACT_GROUP_DONE:
                mName = "GetWorkFlowActGroupDone";
                break;
            case GET_WORKFLOW_ACT_LIST_NOT_SIGNED:
                mName = "GetWorkFlowActListNotSigned";
                break;
            case GET_WORKFLOW_ACT_LIST_SIGNED:
                mName = "GetWorkFlowActListSigned";
                break;
            case GET_WORKFLOW_ACT_LIST_DONE:
                mName = "GetWorkFlowActListDone";
                break;
            case GET_WORKFLOW_PAGE:
                mName = "GetWorkFlowPage";
                break;
            case GET_WORKFLOW_SEND_ROUTER:
                mName = "GetWorkFlowSendRouter";
                break;
            case GET_WORKFLOW_SEND_TASK_ACTOR:
                mName = "GetWorkFlowSendTaskActor";
                break;
            case GET_WORKFLOW_BACK_ROUTER:
                mName = "GetWorkFlowBackRouter";
                break;
            case GET_WORKFLOW_BACK_TASK_ACTOR:
                mName = "GetWorkFlowBackTaskActor";
                break;
            case PDF_FILE_COPY:
                mName = "PdfFileCopy";
                break;
            case PDF_FILE_EXISTS_CHECK:
                mName = "PdfFileExistsCheck";
                break;
            case PDF_FILE_FOLDER_CHECK:
                mName = "PdfFileFolderCheck";
                break;
            case PDF_FILE_REMOVE:
                mName = "PdfFileRemove";
                break;
            case PUSH_SEND:
                mName = "PushSend";
                break;
            case WORKFLOW_BACK:
                mName = "WorkFlowBack";
                break;
            case WORKFLOW_FINISH:
                mName = "WorkFlowFinish";
                break;
            case WORKFLOW_SEND:
                mName = "WorkFlowSend";
                break;
            case WORKFLOW_SIGNIN:
                mName = "WorkFlowSignIn";
                break;
            case WORKFLOW_TASK_FINISH:
                mName = "WorkFlowTaskFinish";
                break;
            case UPLOAD_IMAGE:
                mName = "UploadResume";
                break;
        }

        return mName;
    }

    public static String getServiceUrl(int service, Context context) throws Exception{
        String url = Configuration.SERVER_URL;
        if(Validation.IsNullOrEmpty(url)){
            throw new Exception(context.getString(R.string.no_server_info));
        }
        switch(service){
            case LOGIN:
            case GET_NOTICE_LIST:
            case GET_NOTICE:
            case GET_ATTACHMENT_FILE_LIST:
            case ADD_NOTICE_FEEDBACK:
            case ATTACH_FILE_EXISTS_CHECK:
            case GET_WORKFLOW_ACT_GROUP_NOT_SIGNED:
            case GET_WORKFLOW_ACT_GROUP_SIGNED:
            case GET_WORKFLOW_ACT_GROUP_DONE:
            case GET_WORKFLOW_ACT_LIST_NOT_SIGNED:
            case GET_WORKFLOW_ACT_LIST_SIGNED:
            case GET_WORKFLOW_ACT_LIST_DONE:
            case GET_WORKFLOW_PAGE:
            case GET_WORKFLOW_SEND_ROUTER:
            case GET_WORKFLOW_SEND_TASK_ACTOR:
            case GET_WORKFLOW_BACK_ROUTER:
            case GET_WORKFLOW_BACK_TASK_ACTOR:
            case PDF_FILE_COPY:
            case PDF_FILE_EXISTS_CHECK:
            case PDF_FILE_FOLDER_CHECK:
            case PDF_FILE_REMOVE:
            case PUSH_SEND:
            case WORKFLOW_BACK:
            case WORKFLOW_FINISH:
            case WORKFLOW_SEND:
            case WORKFLOW_SIGNIN:
            case WORKFLOW_TASK_FINISH:
            case UPLOAD_IMAGE:
                url += "AndroidQuery.asmx";
                break;
            case VERSION_PATH:
                url += "Version/version.json";
                break;
            case UPDATE_PATH:
                url += "Version/";
                break;
        }
        return url;
    }

    public static String getFullMethodName(int service){
        return NAMESPACE + getMethodName(service);
    }

}
