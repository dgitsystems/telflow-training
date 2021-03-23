package com.telflow.process.training.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Mybatis interface for queries pertaining to the TEL_MP_ORCHESTRATE table.
 */
public interface ExecutionControlMapper {

    @Update("CREATE TABLE IF NOT EXISTS TEL_MP_ORCHESTRATE "
            + "( BUSINESS_INTERACTION_ID_ VARCHAR(100), "
            + "  PROCESS_ID_ VARCHAR(100), "
            + "  PROCESS_NARRATIVE_ VARCHAR(200), "
            + "  MAPPING_KEY_ VARCHAR(40), "
            + "  EXECUTION_IF_RUNNING_ VARCHAR(10), "
            + "  STATUS_ VARCHAR(20), "
            + "  PROCESS_OUTCOME_ VARCHAR(10), "
            + "  PRIMARY KEY (BUSINESS_INTERACTION_ID_, PROCESS_ID_, MAPPING_KEY_))")
    void createTable();

    @Insert("INSERT INTO TEL_MP_ORCHESTRATE (" +
            "BUSINESS_INTERACTION_ID_, PROCESS_ID_, PROCESS_NARRATIVE_, " +
            "MAPPING_KEY_, EXECUTION_IF_RUNNING_, STATUS_, PROCESS_OUTCOME_) " +
            "VALUES " +
            "(#{businessInteractionId}, #{processId}, #{processNarrative}, #{mappingKey}, #{executionIfRunning}, " +
            "#{status}, #{processOutcome})")
    void insert(ControlEntry entry);

    @Update("UPDATE TEL_MP_ORCHESTRATE SET PROCESS_NARRATIVE_ = #{processNarrative}, " +
            "EXECUTION_IF_RUNNING_ = #{executionIfRunning}, STATUS_ = #{status}, " +
            "PROCESS_OUTCOME_ = #{processOutcome} " +
            "WHERE BUSINESS_INTERACTION_ID_ = #{businessInteractionId} AND PROCESS_ID_ = #{processId} " +
            "AND MAPPING_KEY_ = #{mappingKey} ")
    void update(ControlEntry entry);

    @Update("UPDATE TEL_MP_ORCHESTRATE SET STATUS_ = #{status} " +
            "WHERE BUSINESS_INTERACTION_ID_ = #{businessInteractionId} AND PROCESS_ID_ = #{processId} " +
            "AND MAPPING_KEY_ = #{mappingKey}")
    void updateStatus(@Param("businessInteractionId") String businessInteractionId,
                      @Param("processId") String processId,
                      @Param("mappingKey") String mappingKey,
                      @Param("status") ControlEntry.Status newStatus);

    @Select("SELECT BUSINESS_INTERACTION_ID_, PROCESS_ID_, PROCESS_NARRATIVE_, MAPPING_KEY_, EXECUTION_IF_RUNNING_, " +
            "STATUS_, PROCESS_OUTCOME_ FROM TEL_MP_ORCHESTRATE " +
            "WHERE BUSINESS_INTERACTION_ID_ = #{businessInteractionId} AND PROCESS_ID_ = #{processId} " +
            "AND MAPPING_KEY_ = #{mappingKey}")
    @Results({
            @Result(property = "businessInteractionId", column = "BUSINESS_INTERACTION_ID_"),
            @Result(property = "processId", column = "PROCESS_ID_"),
            @Result(property = "processNarrative", column = "PROCESS_NARRATIVE_"),
            @Result(property = "mappingKey", column = "MAPPING_KEY_"),
            @Result(property = "executionIfRunning", column = "EXECUTION_IF_RUNNING_"),
            @Result(property = "status", column = "STATUS_"),
            @Result(property = "processOutcome", column = "PROCESS_OUTCOME_")
    })
    ControlEntry get(@Param("businessInteractionId") String businessInteractionId,
                     @Param("processId") String processId, @Param("mappingKey") String mappingKey);

    @Select("SELECT BUSINESS_INTERACTION_ID_, PROCESS_ID_, PROCESS_NARRATIVE_, MAPPING_KEY_, EXECUTION_IF_RUNNING_, " +
            "STATUS_, PROCESS_OUTCOME_ FROM TEL_MP_ORCHESTRATE " +
            "WHERE BUSINESS_INTERACTION_ID_ = #{businessInteractionId} AND PROCESS_ID_ = #{processId}")
    @Results({
            @Result(property = "businessInteractionId", column = "BUSINESS_INTERACTION_ID_"),
            @Result(property = "processId", column = "PROCESS_ID_"),
            @Result(property = "processNarrative", column = "PROCESS_NARRATIVE_"),
            @Result(property = "mappingKey", column = "MAPPING_KEY_"),
            @Result(property = "executionIfRunning", column = "EXECUTION_IF_RUNNING_"),
            @Result(property = "status", column = "STATUS_"),
            @Result(property = "processOutcome", column = "PROCESS_OUTCOME_")
    })
    List<ControlEntry> getAll(@Param("businessInteractionId") String businessInteractionId,
                              @Param("processId") String processId);

}
