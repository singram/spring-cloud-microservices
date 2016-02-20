package srai.common.micro.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import srai.common.micro.service.util.ServiceResponseTimer;

public class ManagedResponseControllerBase {
  @Autowired
  protected ServiceResponseTimer setProcTimeBean;

  protected int errorChance = 0;

  protected final Logger logger = LoggerFactory.getLogger( getClass() );

  protected int controlResponseTimeAndError() {
    int pt = setProcTimeBean.calculateProcessingTime();
    sleep(pt);
    if ((int)(Math.random() * 100) < errorChance) {
      throw new RuntimeException("Indeterminate error!!!!");
    }
    return pt;
  }

  private void sleep(int pt) {
    try {
      Thread.sleep(pt);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Set response time for controller
   * Sample usage:
   *
   *  curl "http://localhost:8080/set-processing-time?minMs=1000&maxMs=2000"
   *
   * @param minMs
   * @param maxMs
   */
  @RequestMapping("/set-processing-time")
  @ResponseBody public void setProcessingTime(
      @RequestParam(value = "minMs", required = true) int minMs,
      @RequestParam(value = "maxMs", required = true) int maxMs) {

    logger.info("/set-processing-time called: {} - {} ms", minMs, maxMs);

    setProcTimeBean.setDefaultProcessingTime(minMs, maxMs);
  }

  /**
   * Set change of system error for controller
   * Sample usage:
   *
   *  curl "http://localhost:8080/set-error?percentage=20"
   *
   * @param percentage
   */
  @RequestMapping("/set-error")
  @ResponseBody public void setProcessingTime(
      @RequestParam(value = "percentage", required = true) int percentage) {
    if (percentage < 0) {
      percentage = 0;
    }
    else if (percentage > 100) {
      percentage = 100;
    }
    logger.info("/set-error called: {} %", percentage);
    this.errorChance = percentage;
  }
}
