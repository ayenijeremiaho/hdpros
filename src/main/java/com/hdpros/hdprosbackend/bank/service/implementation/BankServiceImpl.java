package com.hdpros.hdprosbackend.bank.service.implementation;

import com.hdpros.hdprosbackend.bank.dto.BankListResponse;
import com.hdpros.hdprosbackend.bank.dto.DataItem;
import com.hdpros.hdprosbackend.bank.dto.Meta;
import com.hdpros.hdprosbackend.bank.model.BankDetails;
import com.hdpros.hdprosbackend.bank.repository.BankDetailsRepository;
import com.hdpros.hdprosbackend.bank.service.BankService;
import com.hdpros.hdprosbackend.providers.paystack.PaystackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BankServiceImpl implements BankService {

    private final PaystackService paystackService;
    private final BankDetailsRepository bankDetailsRepository;

    public BankServiceImpl(PaystackService paystackService, BankDetailsRepository bankDetailsRepository) {
        this.paystackService = paystackService;
        this.bankDetailsRepository = bankDetailsRepository;
    }

    //Executes first at application startup
    @Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> updateBankTable();
    }

    @Override
    public void updateBankTable() {
        log.info("Getting total banks");
        long total = bankDetailsRepository.count();
        log.info("Total banks gotten is {}", total);

        if (total < 1) {
            log.info("Bank list is empty, making call to get banks");

            BankListResponse listResponse = paystackService.getAllBanks(null);

            log.info("Retrieved Bank list...");

            saveBanks(listResponse);

            if (Objects.nonNull(listResponse.getMeta())) {
                Meta meta = listResponse.getMeta();
                if (!meta.getNext().isEmpty()) {
                    listResponse = paystackService.getAllBanks(meta.getNext());
                    saveBanks(listResponse);
                }
            }
        }
    }

    @Override
    public List<BankDetails> getAllBanks() {
        return bankDetailsRepository.findAll();
    }

    private void saveBanks(BankListResponse listResponse) {
        if (Objects.nonNull(listResponse.getData())) {
            List<DataItem> dataItemList = listResponse.getData();
            List<BankDetails> bankDetailsList = createBankDetailsList(dataItemList);

            log.info("Saving Bank list...");
            bankDetailsRepository.saveAll(bankDetailsList);
            log.info("Finished saving Bank list.");
        }
    }

    private List<BankDetails> createBankDetailsList(List<DataItem> dataItemList) {
        return dataItemList.stream().map(this::createBankDetails).collect(Collectors.toList());
    }

    private BankDetails createBankDetails(DataItem dataItem) {
        BankDetails bankDetails = new BankDetails();

        bankDetails.setBankCode(dataItem.getCode());
        bankDetails.setBankName(dataItem.getName());

        return bankDetails;
    }
}
