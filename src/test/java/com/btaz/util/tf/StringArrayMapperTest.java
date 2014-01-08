package com.btaz.util.tf;

import com.btaz.util.tf.StringArrayMapper;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * User: msundell
 */
public class StringArrayMapperTest {
    @Test
    public void testWithTwoFieldArraysShouldGenerateAValidRemapTable() throws Exception {
        // given
        String [] fields1 = {"id", "name", "type", "value"};
        String [] fields2 = {"type", "value", "id", "name"};

        // when
        int [] remap = StringArrayMapper.createRemapTable(fields1, fields2);

        // then
        assertThat(remap, is(not(nullValue())));
        assertThat(fields2[remap[0]], is(equalTo("id")));
        assertThat(fields2[remap[1]], is(equalTo("name")));
        assertThat(fields2[remap[2]], is(equalTo("type")));
        assertThat(fields2[remap[3]], is(equalTo("value")));
    }
}