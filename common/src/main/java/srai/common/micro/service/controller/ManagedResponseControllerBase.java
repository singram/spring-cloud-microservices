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

  protected final Logger logger = LoggerFactory.getLogger( getClass() );

  protected int controlResponseTime() {
    int pt = setProcTimeBean.calculateProcessingTime();
    sleep(pt);
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
   * Sample usage:
   *
   *  curl "http://localhost:10002/set-processing-time?minMs=1000&maxMs=2000"
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
}
