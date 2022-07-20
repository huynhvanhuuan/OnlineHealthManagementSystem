package vn.edu.hcmuaf.fit.service;

import java.util.List;

import vn.edu.hcmuaf.fit.handle.AppBaseResult;
import vn.edu.hcmuaf.fit.handle.AppResult;
import vn.edu.hcmuaf.fit.model.Request;
import vn.edu.hcmuaf.fit.model.User;

public interface RequestService {
	AppResult<List<Request>> getRequests();
	AppResult<Request> getRequest(Long id);
	AppBaseResult createRequest(Request request,User user);
	AppBaseResult updateRequest(Request request);
	AppBaseResult updateStatus(Long id, int status);
    AppBaseResult removeRequest(Long id);
    AppResult<List<Request>>getRequest(String id);
}