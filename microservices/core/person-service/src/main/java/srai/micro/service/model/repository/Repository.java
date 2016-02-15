package srai.micro.service.model.repository;

import srai.micro.service.model.Cachable;

public interface Repository<V extends Cachable> {

  public void put(V obj);

  public V get(V key);

  public void delete(V key);

}