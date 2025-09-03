package com.manh.ecommerce_java.services;

import com.manh.ecommerce_java.dtos.CourierResult;
import com.manh.ecommerce_java.repositories.OrderRepository;
import com.trackingmore.TrackingMore;
import com.trackingmore.model.TrackingMoreResponse;
import com.trackingmore.model.courier.Courier;
import com.trackingmore.model.courier.DetectParams;
import com.trackingmore.model.tracking.CreateTrackingParams;
import com.trackingmore.model.tracking.GetTrackingResultsParams;
import com.trackingmore.model.tracking.Tracking;
import com.trackingmore.exception.TrackingMoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TrackingShipmentService {

    @Value("${trackingMore.apiKey}")
    private String apiKey;

    @Autowired
    private OrderRepository orderRepository;

    public Set<CourierResult> getAllCouriers() {
        try {
            Set<CourierResult> courierResults = new HashSet<>();
            TrackingMore trackingMore = new TrackingMore(apiKey);
            TrackingMoreResponse<List<Courier>> result = trackingMore.couriers.getAllCouriers();
            List<Courier> couriers = result.getData();
            if (CollectionUtils.isEmpty(couriers)) {
                return new HashSet<>();
            }
                for (Courier courier : couriers) {
                    CourierResult courierResult = new CourierResult();
                    courierResult.setCourierCode(courier.getCourierCode());
                    courierResult.setCourierName(courier.getCourierName());
                    courierResult.setCourierUrl(courier.getCourierUrl());
                    courierResult.setCourierPhone(courier.getCourierPhone());

                    courierResult.setRequired(courier.getTrackingRequiredFields()); // Hoặc method tương tự

                    courierResults.add(courierResult);
                }
            return courierResults;
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
            return new HashSet<>();
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
            return new HashSet<>();
        }
    }

    public void detectCouriers(String trackingNumber) {
        try {
            TrackingMore trackingMore = new TrackingMore(apiKey);
            DetectParams detectParams = new DetectParams();
            detectParams.setTrackingNumber(trackingNumber);
            TrackingMoreResponse<List<Courier>> result = trackingMore.couriers.detect(detectParams);
            System.out.println(result.getMeta().getCode());
            List<Courier> couriers = result.getData();
            for (Courier courier : couriers) {
                String courierName = courier.getCourierName();
                String courierCode = courier.getCourierCode();
                System.out.println(courierName + "---" + courierCode);
            }
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
        }
    }

    public void createTracking(String trackingNumber, String courierCode) {
        try {
            TrackingMore trackingMore = new TrackingMore(apiKey);
            CreateTrackingParams createTrackingParams = new CreateTrackingParams();
            createTrackingParams.setTrackingNumber(trackingNumber);
            createTrackingParams.setCourierCode(courierCode);
            TrackingMoreResponse<Tracking> result = trackingMore.trackings.CreateTracking(createTrackingParams);
            System.out.println(result.getMeta().getCode());
            if (result.getData() != null) {
                Tracking trackings = result.getData();
                System.out.println(trackings);
                System.out.println(trackings.getTrackingNumber());
            }
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
        }
    }
    public void deleteTracking(String trackingNumber) {
        try {
            TrackingMore trackingMore = new TrackingMore(apiKey);
            TrackingMoreResponse result = trackingMore.trackings.DeleteTrackingByID(trackingNumber);
            System.out.println(result);
            System.out.println(result.getMeta().getCode());
            if(result.getData() != null) {
                Tracking trackings = (Tracking) result.getData();
                System.out.println(trackings);
                System.out.println(trackings.getTrackingNumber());
            }
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
        }
    }
    public void RetrackTrackingByID(String trackingNumber) {
        try {
            TrackingMore trackingMore = new TrackingMore(apiKey);
            TrackingMoreResponse result = trackingMore.trackings.RetrackTrackingByID(trackingNumber);
            System.out.println(result);
            System.out.println(result.getMeta().getCode());
            if (result.getData() != null) {
                Tracking tracking = (Tracking) result.getData();
                System.out.println(tracking);
                System.out.println(tracking.getTrackingNumber());
            }
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
        }
    }
    public Tracking getTrackingByTrackingNumber(String trackingNumber1) {
        try {
            TrackingMore trackingMore = new TrackingMore(apiKey);
            GetTrackingResultsParams trackingParams = new GetTrackingResultsParams();
            trackingParams.setTrackingNumbers(trackingNumber1);
            TrackingMoreResponse<List<Tracking>> result = trackingMore.trackings.GetTrackingResults(trackingParams);
            System.out.println(result.getMeta().getCode());
            List<Tracking> trackings = result.getData();
            return trackings.get(0);
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
        }
        return null;
    }

}
