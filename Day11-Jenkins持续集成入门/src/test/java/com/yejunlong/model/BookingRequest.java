package com.yejunlong.model;

import lombok.Data;

@Data
public class BookingRequest {

    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    //内部类
    @Data
    public static class BookingDates{

        private String checkin;
        private String checkout;

        public BookingDates(String checkin, String checkout) {
            this.checkin = checkin;
            this.checkout = checkout;
        }

        public BookingDates() {
        }
    }

    public BookingRequest() {
    }
}
