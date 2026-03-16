package com.app.ventas_api.seguridad.IRepository;

import com.app.ventas_api.seguridad.domain.VerificationCode;
import com.app.ventas_api.seguridad.domain.VerificationCode.CodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IVerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    /**
     * Buscar código válido para un usuario y tipo
     */
    @Query("SELECT v FROM VerificationCode v WHERE v.user.id = :userId AND v.codeType = :codeType AND v.used = false AND v.expiresAt > CURRENT_TIMESTAMP ORDER BY v.createdAt DESC")
    Optional<VerificationCode> findValidCode(@Param("userId") Long userId, @Param("codeType") CodeType codeType);

    /**
     * Buscar código por código y tipo
     */
    @Query("SELECT v FROM VerificationCode v WHERE v.code = :code AND v.codeType = :codeType AND v.used = false AND v.expiresAt > CURRENT_TIMESTAMP")
    Optional<VerificationCode> findByCodeAndType(@Param("code") String code, @Param("codeType") CodeType codeType);

    /**
     * Invalidar códigos anteriores del usuario para un tipo específico
     */
    @Modifying
    @Query("UPDATE VerificationCode v SET v.used = true WHERE v.user.id = :userId AND v.codeType = :codeType")
    void invalidateOldCodes(@Param("userId") Long userId, @Param("codeType") CodeType codeType);

    /**
     * Buscar códigos activos de un usuario
     */
    List<VerificationCode> findByUserIdAndUsedFalseAndExpiresAtAfter(Long userId, java.time.LocalDateTime now);
}
