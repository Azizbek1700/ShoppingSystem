package uz.master.warehouse.services.clientBar;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.master.warehouse.criteria.GenericCriteria;
import uz.master.warehouse.dto.responce.AppErrorDto;
import uz.master.warehouse.dto.responce.DataDto;
import uz.master.warehouse.repository.clientBar.ClientBarRepository;
import uz.master.warehouse.session.SessionUser;
import uz.master.warehouse.dto.comment.CommentCreateDto;
import uz.master.warehouse.dto.comment.CommentDto;
import uz.master.warehouse.dto.comment.CommentUpdateDto;
import uz.master.warehouse.entity.clientBar.Comment;
import uz.master.warehouse.mapper.clientBar.CommentMapper;
import uz.master.warehouse.repository.clientBar.CommentRepository;
import uz.master.warehouse.services.AbstractService;
import uz.master.warehouse.services.GenericCrudService;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService extends AbstractService<CommentRepository, CommentMapper> implements GenericCrudService<Comment, CommentDto, CommentCreateDto, CommentUpdateDto,GenericCriteria, Long> {
    public CommentService(CommentRepository repository, CommentMapper mapper, ClientBarRepository clientBarRepository, SessionUser sessionUser) {
        super(repository, mapper);
        this.clientBarRepository = clientBarRepository;
        this.sessionUser = sessionUser;
    }

    private final ClientBarRepository clientBarRepository;
    private final SessionUser sessionUser;

    @Override
    public DataDto<Long> create(CommentCreateDto createDto) {
        boolean existsBar = clientBarRepository.existsByIdAndDeletedFalse(createDto.getClientBarId());
        if (!existsBar) {
            return new DataDto<>(new AppErrorDto("Client bar not found", HttpStatus.NOT_FOUND));
        }
        Comment comment = mapper.fromCreateDto(createDto);
        comment.setAuthorId(sessionUser.getId());
        repository.save(comment);
        return new DataDto<>(repository.save(comment).getId());
    }

    @Override
    public DataDto<Void> delete(Long id) {

        Optional<Comment> optional = repository.findByIdAndDeletedFalse(id,sessionUser.getOrgId());
        if (!optional.isPresent()) {
            return new DataDto<>(new AppErrorDto("Comment not found", HttpStatus.NOT_FOUND));
        }
        repository.deleteComment(id);
        return new DataDto<>(true);
    }


    @Override
    public DataDto<Long> update(CommentUpdateDto updateDto) {
        Optional<Comment> optionalComment = repository.findById(updateDto.getId());
        if (!optionalComment.isPresent()) {
            return new DataDto<>(new AppErrorDto(HttpStatus.OK, "Income Product not found", "product"));
        }
        Comment comment = mapper.fromUpdateDto(updateDto, optionalComment.get());
        try {

            Comment save = repository.save(comment);
            return new DataDto<>(save.getId());
        } catch (Exception e) {
            return new DataDto<>(new AppErrorDto(HttpStatus.OK, "Bad credentional", "comment"));

        }
    }


    @Override
    public DataDto<List<CommentDto>> getAll() {
        List<Comment> commentList = repository.findAllByDeletedFalse();
        List<CommentDto> commentDto = mapper.toDto(commentList);
        return new DataDto<>(commentDto);
    }

    @Override
    public DataDto<CommentDto> get(Long id) {
        Optional<Comment> optional = repository.findByIdAndDeletedFalse(id, id);
        if (!optional.isPresent()) {
            return new DataDto<>(new AppErrorDto(HttpStatus.NOT_FOUND, "Comment not found", "product"));
        }
        CommentDto commentDto = mapper.toDto(optional.get());
        return new DataDto<>(commentDto);
    }

    @Override
    public DataDto<List<CommentDto>> getWithCriteria(GenericCriteria criteria) {
        return null;
    }
}
