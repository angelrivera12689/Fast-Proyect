package com.app.ventas_api.seguridad.Service;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * TOTP Service - Maneja la autenticación de dos factores (2FA)
 */
@Service
public class TotpService {

    private final SecretGenerator secretGenerator;
    private final CodeVerifier codeVerifier;
    private final TimeProvider timeProvider;

    public TotpService() {
        this.secretGenerator = new DefaultSecretGenerator();
        this.timeProvider = new SystemTimeProvider();
        this.codeVerifier = new DefaultCodeVerifier(
            new DefaultCodeGenerator(HashingAlgorithm.SHA1),
            timeProvider
        );
    }

    /**
     * Genera un nuevo secreto TOTP para el usuario
     */
    public String generateSecret() {
        return secretGenerator.generate();
    }

    /**
     * Genera el código QR en formato Base64 para que el usuario escanee con su app
     * @param secret El secreto TOTP
     * @param username Nombre de usuario
     * @param issuer Nombre de la aplicación
     * @return String Base64 de la imagen QR
     */
    public String generateQrCodeImage(String secret, String username, String issuer) {
        try {
            QrData data = new QrData.Builder()
                    .label(username)
                    .secret(secret)
                    .issuer(issuer)
                    .algorithm(HashingAlgorithm.SHA1)
                    .digits(6)
                    .period(30)
                    .build();

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data.getUri(), BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR code: " + e.getMessage());
        }
    }

    /**
     * Valida un código TOTP
     * @param secret El secreto guardado del usuario
     * @param code El código de 6 dígitos ingreso el usuario
     * @return true si el código es válido
     */
    public boolean validateCode(String secret, String code) {
        return codeVerifier.isValidCode(secret, code);
    }

    /**
     * Obtiene el tiempo restante del código actual
     * @return segundos restantes
     */
    public int getTimeRemaining() {
        long currentTime = System.currentTimeMillis() / 1000;
        return 30 - (int) (currentTime % 30);
    }
}
