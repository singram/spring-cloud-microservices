package srai.micro.service.model.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import srai.micro.service.model.CachablePerson;

@Service
public class PersonRepository implements Repository<CachablePerson> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PersonRepository.class);

  @Autowired
  RedisTemplate<String, CachablePerson> redisTemplate;

  @Override
  public void put(CachablePerson person) {
    redisTemplate.opsForHash().put(person.getObjectKey(), person.getKey(), person);
  }

  @Override
  public void delete(CachablePerson key) {
    redisTemplate.opsForHash().delete(key.getObjectKey(), key.getKey());
  }

  @Override
  public CachablePerson get(CachablePerson key) {
    return (CachablePerson) redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());
  }
}
