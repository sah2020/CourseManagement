package uz.exadel.mapper.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.TeacherDTO;
import uz.exadel.entity.Course;
import uz.exadel.entity.Teacher;
import uz.exadel.enums.TeacherPositionEnum;
import uz.exadel.mapper.TeacherMapper;
import uz.exadel.repository.CourseRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TeacherMapperImpl implements TeacherMapper {
    private final CourseRepository courseRepository;

    public TeacherMapperImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Teacher teacherFromTeacherDTO(TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher();
        return convert(teacherDTO, teacher);
    }

    @Override
    public Teacher teacherFromTeacherDTOUpdate(TeacherDTO teacherDTO, Teacher teacher) {
        return convert(teacherDTO, teacher);
    }

    private Teacher convert(TeacherDTO teacherDTO, Teacher teacher) {
        if (StringUtils.hasText(teacherDTO.getEmail())) {
            teacher.setEmail(teacherDTO.getEmail());
        }
        if (StringUtils.hasText(teacherDTO.getOfficePhoneNumber())) {
            teacher.setOfficePhoneNumber(teacherDTO.getOfficePhoneNumber());
        }
        List<String> positions = teacherDTO.getPositions();
        if (!positions.isEmpty()) {
            Set<TeacherPositionEnum> positionEnumSet = positions.stream().map(TeacherPositionEnum::valueOf).collect(Collectors.toSet());
            teacher.setPositions(positionEnumSet);
        }
        teacher.setFullName(teacherDTO.getFullName());
        teacher.setSchoolId(UUID.fromString(teacherDTO.getSchoolId()));

        List<String> courseIdsString = teacherDTO.getCourses();
        Set<UUID> courseIds = courseIdsString.stream().map(UUID::fromString).collect(Collectors.toSet());
        Set<Course> courses = courseRepository.findByIdIn(courseIds);
        teacher.setCourses(courses);

        return teacher;
    }
}