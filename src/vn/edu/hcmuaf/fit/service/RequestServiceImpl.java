package vn.edu.hcmuaf.fit.service;

import java.util.ArrayList;
import java.util.List;

import vn.edu.hcmuaf.fit.dao.*;
import vn.edu.hcmuaf.fit.handle.*;
import vn.edu.hcmuaf.fit.model.*;

import static vn.edu.hcmuaf.fit.constant.RequestStatusConstant.*;

public class RequestServiceImpl implements RequestService {
	private final RequestDAO requestDAO;
	
	public RequestServiceImpl() {
		requestDAO = RequestDAOImpl.getInstance();
	}

	@Override
	public AppResult<List<Request>> getRequests() {
		List<Request> requests = requestDAO.findAll();
		return new AppResult<>(true, "Success", requests);
	}

	@Override
	public AppResult<List<Request>> getRequests(String userId) {
		List<Request> requests = requestDAO.findByUserId(userId);
		return new AppResult<>(true, "Success", requests);
	}

	@Override
	public AppResult<Request> getRequest(Long id) {
		Request request = requestDAO.findById(id);

		if (request == null) {
			return new AppResult<>(false, "Request not found", null);
		}

		return new AppResult<>(true, "Success", request);
	}

	@Override
	public AppBaseResult createRequest(Request request) {
		requestDAO.save(request);

		return new AppBaseResult(true, "Tạo yêu cầu thành công");
	}

	@Override
	public AppBaseResult updateRequest(Request request) {
		requestDAO.save(request);
		return new AppBaseResult(true, "Cập nhật yêu cầu thành công");
	}

	@Override
	public AppBaseResult updateStatus(Long id, int status) {
		Request updateRequest = requestDAO.findById(id);

		if (updateRequest == null) {
			return new AppBaseResult(false, "Request not found");
		}

		updateRequest.setStatus(status);
		requestDAO.save(updateRequest);

		return new AppBaseResult(true, "Cập nhật thành công!");
	}

	@Override
	public AppBaseResult removeRequest(Long id) {
		Request request = requestDAO.findById(id);

		if (request == null) {
			return new AppBaseResult(false, "Request not found");
		}

		requestDAO.remove(id);

		return new AppBaseResult(true, "Xóa thành công!");
	}

	@Override
	public AppResult<List<Request>> search(String keyword) {
		List<Request> requests = new ArrayList<>();

		if (keyword.isEmpty()) {
			return new AppResult<>(true, "Success", requestDAO.findAll());
		}

		loop: for (Request request : requestDAO.findAll()) {
			List<Patient> patients = request.getPatients();

			switch (request.getStatus()) {
				case 0 -> {
					if (PENDING.message().toLowerCase().contains(keyword.toLowerCase())) {
						requests.add(request);
						continue;
					}
				}
				case 1 -> {
					if (SUBMITTED.message().toLowerCase().contains(keyword.toLowerCase())) {
						requests.add(request);
						continue;
					}
				}
				case 2 -> {
					if (REQUEST_AMBULANCE.message().toLowerCase().contains(keyword.toLowerCase())) {
						requests.add(request);
						continue;
					}
				}
				case 3 -> {
					if (AMBULANCE_MOVING.message().toLowerCase().contains(keyword.toLowerCase())) {
						requests.add(request);
						continue;
					}
				}
				case 4 -> {
					if (AMBULANCE_ARRIVED.message().toLowerCase().contains(keyword.toLowerCase())) {
						requests.add(request);
						continue;
					}
				}
				case 5 -> {
					if (COMPLETED.message().contains(keyword)) {
						requests.add(request);
						continue;
					}
				}
			}

			if (request.getId().toString().contains(keyword) ||
				request.getPhone().contains(keyword) ||
				request.getAddress().toLowerCase().contains(keyword.toLowerCase()) ||
				request.getProblemDescription().toLowerCase().contains(keyword.toLowerCase())) {
				requests.add(request);
				continue;
			}

			for (Patient patient : patients) {
				if (patient.getId().contains(keyword) ||
					patient.getFullname().toLowerCase().contains(keyword.toLowerCase()) ||
					String.valueOf(patient.getAge()).contains(keyword) ||
					(patient.isMale() && keyword.equalsIgnoreCase("nam")) ||
					(!patient.isMale() && keyword.equalsIgnoreCase("nữ"))) {
					requests.add(request);
					continue loop;
				}
			}
		}
		return new AppResult<>(true, "Success", requests);
	}

}
