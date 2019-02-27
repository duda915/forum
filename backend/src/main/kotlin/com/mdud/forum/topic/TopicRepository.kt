package com.mdud.forum.topic

import org.springframework.data.repository.CrudRepository

interface TopicRepository : CrudRepository<Topic, Long>