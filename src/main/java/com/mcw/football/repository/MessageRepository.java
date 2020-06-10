package com.mcw.football.repository;

import com.mcw.football.domain.Message;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.ChatMessageDto;
import com.mcw.football.domain.dto.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select new com.mcw.football.domain.dto.MessageDTO(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "group by m")
    Page<MessageDTO> findAll(Pageable pageable, @Param("user") User user);
//
    @Query("select new com.mcw.football.domain.dto.MessageDTO(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    Page<MessageDTO> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

    @Query("select new com.mcw.football.domain.dto.MessageDTO(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Page<MessageDTO> findAllWhereAuthor(Pageable pageable, @Param("user") User user, @Param("author")User author);

    List<Message> findAllByChatTypeOrderById(String chatType, Pageable pageable);
}
