package com.example.TheFit.totalworkouts.service;

import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.totalworkouts.domain.TotalWorkOuts;
import com.example.TheFit.totalworkouts.dto.TotalWorkOutsReqDto;
import com.example.TheFit.totalworkouts.dto.TotalWorkOutsResDto;
import com.example.TheFit.totalworkouts.mapper.TotalWorkOutsMapper;
import com.example.TheFit.totalworkouts.repository.TotalWorkOutsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TotalWorkOutsService {
    private final TotalWorkOutsRepository totalWorkOutsRepository;
    private final TotalWorkOutsMapper totalWorkOutsMapper = TotalWorkOutsMapper.INSTANCE;

    @Autowired
    public TotalWorkOutsService(TotalWorkOutsRepository totalWorkOutsRepository) {
        this.totalWorkOutsRepository = totalWorkOutsRepository;
    }

    public TotalWorkOuts create(TotalWorkOutsReqDto totalWorkOutsReqDto) {
        TotalWorkOuts totalWorkOuts = TotalWorkOutsMapper.INSTANCE.toEntity(totalWorkOutsReqDto);
        totalWorkOutsRepository.save(totalWorkOuts);
        return totalWorkOuts;
    }

    public List<TotalWorkOutsResDto> findAll() {
        List<TotalWorkOuts> totalWorkOuts = totalWorkOutsRepository.findAll();
        return totalWorkOuts.stream()
                .filter(a-> a.delYn.equals("N"))
                .map(totalWorkOutsMapper::toDto)
                .collect(Collectors.toList());
    }
    public TotalWorkOuts update(Long id, TotalWorkOutsReqDto totalWorkOutsReqDto) throws TheFitBizException {
        TotalWorkOuts totalWorkOuts = totalWorkOutsRepository.findById(id)
                .orElseThrow(() -> new TheFitBizException(ErrorCode.NOT_FOUND_TOTALWORKOUT));
        totalWorkOuts.update(totalWorkOutsReqDto);
        return totalWorkOuts;
    }
    public void delete(Long id) {
        TotalWorkOuts totalWorkOuts = totalWorkOutsRepository.findById(id).orElseThrow
                (()-> new TheFitBizException(ErrorCode.NOT_FOUND_TOTALWORKOUT)
        );
        totalWorkOuts.delete();
    }
}